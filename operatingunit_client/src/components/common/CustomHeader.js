import { primaryColor, rnpc } from "../../const/constants";
import Title from "antd/lib/typography/Title";
import React from "react";
import { Header } from "antd/es/layout/layout";

const CustomHeader = () => {
  return (
    <Header
      style={{
        backgroundColor: primaryColor,
        alignItems: "center",
      }}
    >
      <Title level={3} style={{ color: "white", margin: 0 }}>
        {rnpc}
      </Title>
    </Header>
  );
};

export default CustomHeader;
