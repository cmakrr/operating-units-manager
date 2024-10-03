import { Descriptions, Form } from "antd";
import React from "react";
import { updateAndStartOperationFact } from "../../request/Requests";

export const CustomDescriptions = (items) => {
  return (
    <Descriptions
      bordered
      items={items}
      size={"small"}
      labelStyle={{
        color: "black",
        backgroundColor: "#dfede2",
      }}
    />
  );
};

export const CustomFormDescriptions = (
  id,
  factId,
  items,
  onFinish,
  onFailed,
  initialValues,
  formButtonText,
) => {
  return (
    <Form
      name="basic"
      onFinish={(values) => updateAndStartOperationFact(id, factId, values)}
      autoComplete="off"
      fields={initialValues}
    >
      <Descriptions
        bordered
        items={items}
        size={"small"}
        labelStyle={{
          color: "black",
          backgroundColor: "#dfede2",
        }}
      />
    </Form>
  );
};
