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
package com.sap.xsk.hdb.ds.itest.hdbsequence;

import com.sap.xsk.hdb.ds.api.XSKDataStructuresException;
import com.sap.xsk.hdb.ds.facade.IXSKHDBCoreFacade;
import com.sap.xsk.hdb.ds.facade.XSKHDBCoreFacade;
import com.sap.xsk.hdb.ds.itest.model.JDBCModel;
import com.sap.xsk.hdb.ds.itest.module.XSKHDBTestModule;
import com.sap.xsk.hdb.ds.processors.hdbsequence.XSKHDBSequenceDropProcessor;
import com.sap.xsk.utils.XSKHDBUtils;
import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.core.scheduler.api.SynchronizationException;
import org.eclipse.dirigible.database.ds.model.IDataStructureModel;
import org.eclipse.dirigible.repository.local.LocalResource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.sap.xsk.hdb.ds.itest.utils.TestConstants.HANA_DRIVER;
import static com.sap.xsk.hdb.ds.itest.utils.TestConstants.HANA_PASSWORD;
import static com.sap.xsk.hdb.ds.itest.utils.TestConstants.HANA_URL;
import static com.sap.xsk.hdb.ds.itest.utils.TestConstants.HANA_USERNAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XSKHDBSequenceParserHanaITTest {

  private static DataSource datasource;
  private static IXSKHDBCoreFacade facade;

  @BeforeClass
  public static void setUpBeforeClass() throws SQLException {
    JDBCModel model = new JDBCModel(HANA_DRIVER, HANA_URL, HANA_USERNAME,
        HANA_PASSWORD);
    XSKHDBTestModule xskhdbTestModule = new XSKHDBTestModule(model);
    xskhdbTestModule.configure();
    datasource = (DataSource) StaticObjects.get(StaticObjects.DATASOURCE);
    facade = new XSKHDBCoreFacade();
  }

  @Before
  public void setUpBeforeTest() throws SQLException {
    try (Connection connection = datasource.getConnection();
        Statement stmt = connection.createStatement()) {
      DatabaseMetaData metaData = connection.getMetaData();
      String hanaUserName = Configuration.get("hana.username");
      ResultSet table = metaData.getTables(null, hanaUserName, "XSK_DATA_STRUCTURES", null);
      if (table.next()) {
        stmt.executeUpdate(String.format(
            "DELETE FROM \"%s\".\"XSK_DATA_STRUCTURES\" WHERE DS_LOCATION ='/sequence-itest/SampleSequence_HanaXSClassic.hdbsequence' OR DS_LOCATION = '/sequence-itest/SampleSequence_HanaXSClassicDiffSchema.hdbsequence'",
            hanaUserName));
      }
      Configuration.set(IDataStructureModel.DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE, "true");
      facade.clearCache();
    }
  }

  @Test
  public void testHDBSequenceCreateSameSchema() throws XSKDataStructuresException, SynchronizationException, IOException, SQLException {
    LocalResource resource = XSKHDBTestModule.getResources("/usr/local/target/dirigible/repository/root",
        "/registry/public/sequence-itest/SampleSequence_HanaXSClassic.hdbsequence",
        "/sequence-itest/SampleSequence_HanaXSClassic.hdbsequence");
    this.facade.handleResourceSynchronization(resource);
    this.facade.updateEntities();
    try (Connection connection = datasource.getConnection();
        Statement stmt = connection.createStatement()) {
      ResultSet rs = stmt.executeQuery(String
          .format("SELECT COUNT(*) as rawsCount from Sequences WHERE SEQUENCE_NAME = '%s'",
              "sequence-itest::SampleSequence_HanaXSClassic"));
      assertTrue(rs.next());
      assertEquals(1, rs.getInt("rawsCount"));
      stmt.executeUpdate(
          String.format("DROP SEQUENCE %s", XSKHDBUtils.escapeArtifactName(connection, "sequence-itest::SampleSequence_HanaXSClassic")));
      rs = stmt.executeQuery(String
          .format("SELECT COUNT(*) as rawsCount from Sequences WHERE SEQUENCE_NAME = '%s'",
              "sequence-itest::SampleSequence_HanaXSClassic"));
      assertTrue(rs.next());
      assertEquals(0, rs.getInt("rawsCount"));
    }
  }

  @Test
  public void testHDBSequenceCreateDifferentSchema() throws XSKDataStructuresException, SynchronizationException, IOException, SQLException {
    try (Connection connection = datasource.getConnection();
        Statement stmt = connection.createStatement()) {
      String schemaName = "SEQUENCE_ITEST";
      String artifactName = "sequence-itest::SampleSequence_HanaXSClassicDiffSchema";
      stmt.executeUpdate(String.format("CREATE SCHEMA %s", schemaName));

      LocalResource resource = XSKHDBTestModule.getResources("/usr/local/target/dirigible/repository/root",
          "/registry/public/sequence-itest/SampleSequence_HanaXSClassicDiffSchema.hdbsequence",
          "/sequence-itest/SampleSequence_HanaXSClassicDiffSchema.hdbsequence");

      this.facade.handleResourceSynchronization(resource);
      this.facade.updateEntities();

      ResultSet rs = stmt.executeQuery(String
          .format("SELECT COUNT(*) as rawsCount from Sequences WHERE SCHEMA_NAME='%s' AND SEQUENCE_NAME = '%s'",
              schemaName, artifactName));

      assertTrue(rs.next());
      assertEquals(1, rs.getInt("rawsCount"));
      stmt.executeUpdate(
          String.format("DROP SEQUENCE %s", XSKHDBUtils.escapeArtifactName(connection, artifactName, schemaName)));
      rs = stmt.executeQuery(String
          .format("SELECT COUNT(*) as rawsCount from Sequences WHERE SCHEMA_NAME='%s' AND SEQUENCE_NAME = '%s'",
              schemaName, artifactName));
      assertTrue(rs.next());
      assertEquals(0, rs.getInt("rawsCount"));
      stmt.executeUpdate(String.format("DROP SCHEMA %s CASCADE ", schemaName));
    }
  }
}
