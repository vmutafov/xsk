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

import com.sap.xsk.exceptions.XSKArtifactParserException;
import com.sap.xsk.parser.hdbti.exception.XSKHDBTISyntaxErrorException;
import com.sap.xsk.parser.hdbti.models.XSKHDBTIImportModel;
import org.eclipse.dirigible.core.problems.exceptions.ProblemsException;

import java.io.IOException;

public interface IXSKHDBTIParser {
    XSKHDBTIImportModel parse(String location, String content) throws IOException, XSKHDBTISyntaxErrorException, XSKArtifactParserException, ProblemsException;
}
