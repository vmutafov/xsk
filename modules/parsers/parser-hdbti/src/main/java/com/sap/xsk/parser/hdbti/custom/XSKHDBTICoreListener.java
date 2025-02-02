/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company and XSK contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, v2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-FileCopyrightText: 2021 SAP SE or an SAP affiliate company and XSK contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.sap.xsk.parser.hdbti.custom;

import com.sap.xsk.parser.hdbti.core.HdbtiBaseListener;
import com.sap.xsk.parser.hdbti.core.HdbtiParser;
import com.sap.xsk.parser.hdbti.exception.DuplicateFieldNameException;
import com.sap.xsk.parser.hdbti.models.XSKHDBTIImportConfigModel;
import com.sap.xsk.parser.hdbti.models.XSKHDBTIImportModel;

import java.util.*;

public class XSKHDBTICoreListener extends HdbtiBaseListener {

    private final XSKHDBTIImportModel importModel = new XSKHDBTIImportModel();
    private final Set<String> usedFields = new HashSet<>();
    private XSKHDBTIImportConfigModel configModel;

    @Override
    public void enterObjConfig(HdbtiParser.ObjConfigContext ctx) {
        configModel = new XSKHDBTIImportConfigModel();
    }

    @Override
    public void exitObjConfig(HdbtiParser.ObjConfigContext ctx) {
        List<XSKHDBTIImportConfigModel.Pair> pairs = new ArrayList<>();

        configModel.setUseHeaderNames(false);
        configModel.setHeader(false);
        configModel.setDistinguishEmptyFromNull(true);

        for (HdbtiParser.AssignExpressionContext expressionContext :
                ctx.assignExpression()) {
            if (expressionContext.assignTable() != null) {
                String tableName = expressionContext.assignTable().STRING().getText();
                configModel.setTableName(handleStringLiteral(tableName));
            } else if (expressionContext.assignSchema() != null) {
                String schemaName = expressionContext.assignSchema().STRING().getText();
                configModel.setSchemaName(handleStringLiteral(schemaName));
            } else if (expressionContext.assignFile() != null) {
                String fileName = expressionContext.assignFile().STRING().getText();
                configModel.setFileName(handleStringLiteral(fileName));
            } else if (expressionContext.assignHeader() != null) {
                String header = expressionContext.assignHeader().BOOLEAN().getText();
                configModel.setHeader(Boolean.parseBoolean(header));
            } else if (expressionContext.assignUseHeaderNames() != null) {
                String useHeaderNames = expressionContext.assignUseHeaderNames().BOOLEAN().getText();
                configModel.setUseHeaderNames(Boolean.parseBoolean(useHeaderNames));
            } else if (expressionContext.assignDelimField() != null) {
                String delimField = expressionContext.assignDelimField().STRING().getText();
                configModel.setDelimField(handleStringLiteral(delimField));
            } else if (expressionContext.assignDelimEnclosing() != null) {
                String delimEnclosing = expressionContext.assignDelimEnclosing().STRING().toString();
                configModel.setDelimEnclosing(handleStringLiteral(delimEnclosing));
            } else if (expressionContext.assignDistinguishEmptyFromNull() != null) {
                String distinguishEmptyFromNull = expressionContext.assignDistinguishEmptyFromNull().BOOLEAN().getText();
                configModel.setDistinguishEmptyFromNull(Boolean.parseBoolean(distinguishEmptyFromNull));
            } else if (expressionContext.assignKeys() != null) {
                List<XSKHDBTIImportConfigModel.Pair> pairsToBeAdd = new ArrayList<>();
                expressionContext.assignKeys().keyArr().pair().forEach(el -> {
                    if (pairs.isEmpty()) {
                        XSKHDBTIImportConfigModel.Pair newPair = new XSKHDBTIImportConfigModel.Pair(handleStringLiteral(el.pairKey().getText()), new ArrayList<>(Collections.singletonList(handleStringLiteral(el.pairValue().getText()))));
                        pairs.add(newPair);
                    } else {
                        for (XSKHDBTIImportConfigModel.Pair pair : pairs) {
                            if (pair.getColumn().equals(handleStringLiteral(el.pairKey().getText()))) {
                                pair.getValues().add(handleStringLiteral(el.pairValue().getText()));
                            } else {
                                XSKHDBTIImportConfigModel.Pair newPair = new XSKHDBTIImportConfigModel.Pair(handleStringLiteral(el.pairKey().getText()), new ArrayList<>(Collections.singletonList(handleStringLiteral(el.pairValue().getText()))));
                                pairsToBeAdd.add(newPair);
                            }
                        }
                    }
                    pairs.addAll(pairsToBeAdd);
                });
            }
        }

        configModel.setKeys(pairs);
        importModel.getConfigModels().add(configModel);
        usedFields.clear();
    }

    @Override
    public void enterAssignExpression(HdbtiParser.AssignExpressionContext ctx) {
        String currentObjField = ctx.getChild(0).getChild(0).getText();
        if (usedFields.contains(currentObjField)) {
            throw new DuplicateFieldNameException(String.format("Duplicate fields: Field with name: '%s' is already in use.", currentObjField));
        } else {
            usedFields.add(currentObjField);
        }
    }

    public XSKHDBTIImportModel getImportModel() {
        return importModel;
    }

    private String handleStringLiteral(String value) {
        if (value != null && value.length() > 1) {
            String subStr = value.substring(1, value.length() - 1);
            String escapedQuote = subStr.replace("\\\"", "\"");
            return escapedQuote.replace("\\\\", "\\");
        }
        return value;
    }
}
