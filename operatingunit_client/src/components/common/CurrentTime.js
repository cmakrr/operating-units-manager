import { Row, Space, Typography } from "antd";
import { ClockCircleOutlined } from "@ant-design/icons";
import React, { useState, useEffect } from "react";
import { primaryColor } from "../../const/constants";

const { Text } = Typography;

function Clock() {
  const [time, setTime] = useState(new Date());

  useEffect(() => {
    const interval = setInterval(() => setTime(new Date()), 1000);
    return () => clearInterval(interval);
  }, []);

  const formattedTime = time.toLocaleTimeString();
  const formattedDate = time.toLocaleDateString("ru-RU", {
    day: "numeric",
    month: "long",
    year: "numeric",
  });

  return (
    <Row justify="center">
      <Space align="center">
        <ClockCircleOutlined />
        <Text type="secondary">
          Текущее время:
          <span style={{ color: primaryColor }}> {formattedTime}</span>{" "}
          {formattedDate}
        </Text>
      </Space>
    </Row>
  );
}
export default Clock;
