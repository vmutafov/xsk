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
import com.sap.xsk.hdb.ds.itest.module.XSKHDBTestModule;
import com.sap.xsk.hdb.ds.itest.model.JDBCModel;
import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.core.scheduler.api.SynchronizationException;
import org.eclipse.dirigible.database.ds.model.IDataStructureModel;
import org.eclipse.dirigible.repository.local.LocalResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * XSKHDBSequenceParserITCase will test only sequence
 * creation due existing sequence drop logic that is never invoked.
 * Existing sequence alter logic is not checked due db limitations on retrieving sequence info.
 */
public class XSKHDBSequenceParserPostgreSQLITTest {

  private static PostgreSQLContainer jdbcContainer;
  private static DataSource datasource;
  private static IXSKHDBCoreFacade facade;


  @BeforeClass
  public static void setUp() throws SQLException {
    Network network = Network.newNetwork();
    jdbcContainer =
        new PostgreSQLContainer<>("postgres:alpine");
    jdbcContainer.start();
    JDBCModel model = new JDBCModel(jdbcContainer.getDriverClassName(), jdbcContainer.getJdbcUrl(), jdbcContainer.getUsername(),
        jdbcContainer.getPassword());
    XSKHDBTestModule xskhdbTestModule = new XSKHDBTestModule(model);
    xskhdbTestModule.configure();
    datasource = (DataSource) StaticObjects.get(StaticObjects.DATASOURCE);
    facade = new XSKHDBCoreFacade();
    facade.clearCache();
  }

  @Before
  public void setUpBeforeTest() throws SQLException {
    Configuration.set(IDataStructureModel.DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE, "true");
    facade.clearCache();
  }

  @AfterClass
  public static void cleanUp() {
    jdbcContainer.stop();
  }

  @Test
  public void testHDBSequenceCreate() throws XSKDataStructuresException, SynchronizationException, IOException, SQLException {
    LocalResource resource = XSKHDBTestModule.getResources("/usr/local/target/dirigible/repository/root",
        "/registry/public/sequence-itest/SampleSequence_PostgreSQL.hdbsequence",
        "/sequence-itest/SampleSequence_PostgreSQL.hdbsequence");

    this.facade.handleResourceSynchronization(resource);
    this.facade.updateEntities();

    try (Connection connection = datasource.getConnection();
  			Statement stmt = connection.createStatement()) {
	    List<String> dbSequences = new ArrayList<>();
	    ResultSet rs = stmt.executeQuery("SELECT  relname sequence_name FROM  pg_class WHERE  relkind = 'S'");
	    while (rs.next()) {
	      dbSequences.add(rs.getString("sequence_name"));
	    }
	    assertEquals(1, dbSequences.size());
	    assertEquals("sequence-itest::SampleSequence_PostgreSQL", dbSequences.get(0));
	
	    stmt.executeUpdate(String.format("DROP SEQUENCE \"%s\"", dbSequences.get(0)));
	    rs = stmt.executeQuery("SELECT  relname sequence_name FROM  pg_class WHERE  relkind = 'S'");
	    assertFalse(rs.next());
    }
  }


}
