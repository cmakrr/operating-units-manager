import Card from "antd/lib/card/Card";
import { Col, Form, Input, Row, Tag } from "antd";
import React from "react";
import TextArea from "antd/es/input/TextArea";

export const SmallCardWithTag = (tag, color = "volcano") => {
  return (
    <Card bordered={false} size={"small"}>
      <Tag color={color} key={tag}>
        {tag?.toUpperCase()}
      </Tag>
    </Card>
  );
};

export const CardOrCardWithTag = (value, tag) => {
  return value ? (
    <Card bordered={false} size={"small"}>
      {value}
    </Card>
  ) : (
    SmallCardWithTag(tag)
  );
};
export const OperationPlanFactCards = (
  planValue,
  planTag,
  factValue,
  factTag,
) => {
  return (
    <Row gutter={16}>
      <Col span={12}>{CardOrCardWithTag(planValue, planTag)}</Col>
      <Col span={12}>{CardOrCardWithTag(factValue, factTag)}</Col>
    </Row>
  );
};

const _CartForOperationFactForm = (val, label) => {
  if (label === "instruments") {
    return (
      <Form.Item name={label}>
        <TextArea autoSize size={"large"} style={{ marginTop: "3px" }} />
      </Form.Item>
    );
  } else {
    return (
      <Form.Item name={label}>
        <Input
          name={label}
          size="small"
          style={{ height: "47px", borderRadius: "7px" }}
        />
      </Form.Item>
    );
  }
};

export const OperationPlanEditableFactCards = (
  planValue,
  planTag,
  factValue,
  factTag,
  label,
) => {
  return (
    <Row gutter={16}>
      <Col span={12} style={{ height: "100%" }}>
        {CardOrCardWithTag(planValue, planTag)}
      </Col>
      <Col span={12} style={{ height: "100%" }}>
        {_CartForOperationFactForm(factValue, label)}
      </Col>
    </Row>
  );
};

export const CenterCard = ({ children }) => {
  return (
    <Card
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      {children}
    </Card>
  );
};
