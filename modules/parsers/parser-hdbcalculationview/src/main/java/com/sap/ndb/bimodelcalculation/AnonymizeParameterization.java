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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.26 at 10:54:28 AM EET 
//


package com.sap.ndb.bimodelcalculation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for AnonymizeParameterization complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AnonymizeParameterization"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="seqColumn" type="{http://www.sap.com/ndb/BaseModelBase.ecore}DbName"/&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="k" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *           &lt;element name="loss" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *           &lt;element name="qidColumn" type="{http://www.sap.com/ndb/BiModelCalculation.ecore}QidColumn" maxOccurs="unbounded"/&gt;
 *           &lt;element name="qidColumnHierarchy" type="{http://www.sap.com/ndb/BiModelCalculation.ecore}QidColumnHierarchy" maxOccurs="unbounded"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="epsilon" type="{http://www.w3.org/2001/XMLSchema}float"/&gt;
 *           &lt;element name="sensitivity" type="{http://www.w3.org/2001/XMLSchema}float"/&gt;
 *           &lt;element name="noisedColumn" type="{http://www.sap.com/ndb/BaseModelBase.ecore}DbName"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnonymizeParameterization", propOrder = {
    "seqColumn",
    "k",
    "loss",
    "qidColumn",
    "qidColumnHierarchy",
    "epsilon",
    "sensitivity",
    "noisedColumn"
})
public class AnonymizeParameterization {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String seqColumn;
  @XmlElement(required = true)
  protected BigInteger k;
  protected double loss;
  @XmlElement(required = true)
  protected List<QidColumn> qidColumn;
  @XmlElement(required = true)
  protected List<QidColumnHierarchy> qidColumnHierarchy;
  protected float epsilon;
  protected float sensitivity;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "token")
  protected String noisedColumn;

  /**
   * Gets the value of the seqColumn property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getSeqColumn() {
    return seqColumn;
  }

  /**
   * Sets the value of the seqColumn property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setSeqColumn(String value) {
    this.seqColumn = value;
  }

  /**
   * Gets the value of the k property.
   *
   * @return possible object is
   * {@link BigInteger }
   */
  public BigInteger getK() {
    return k;
  }

  /**
   * Sets the value of the k property.
   *
   * @param value allowed object is
   *              {@link BigInteger }
   */
  public void setK(BigInteger value) {
    this.k = value;
  }

  /**
   * Gets the value of the loss property.
   */
  public double getLoss() {
    return loss;
  }

  /**
   * Sets the value of the loss property.
   */
  public void setLoss(double value) {
    this.loss = value;
  }

  /**
   * Gets the value of the qidColumn property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the qidColumn property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getQidColumn().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link QidColumn }
   */
  public List<QidColumn> getQidColumn() {
    if (qidColumn == null) {
      qidColumn = new ArrayList<QidColumn>();
    }
    return this.qidColumn;
  }

  /**
   * Gets the value of the qidColumnHierarchy property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the qidColumnHierarchy property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getQidColumnHierarchy().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link QidColumnHierarchy }
   */
  public List<QidColumnHierarchy> getQidColumnHierarchy() {
    if (qidColumnHierarchy == null) {
      qidColumnHierarchy = new ArrayList<QidColumnHierarchy>();
    }
    return this.qidColumnHierarchy;
  }

  /**
   * Gets the value of the epsilon property.
   */
  public float getEpsilon() {
    return epsilon;
  }

  /**
   * Sets the value of the epsilon property.
   */
  public void setEpsilon(float value) {
    this.epsilon = value;
  }

  /**
   * Gets the value of the sensitivity property.
   */
  public float getSensitivity() {
    return sensitivity;
  }

  /**
   * Sets the value of the sensitivity property.
   */
  public void setSensitivity(float value) {
    this.sensitivity = value;
  }

  /**
   * Gets the value of the noisedColumn property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getNoisedColumn() {
    return noisedColumn;
  }

  /**
   * Sets the value of the noisedColumn property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setNoisedColumn(String value) {
    this.noisedColumn = value;
  }

}
