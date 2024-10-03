import { Row, Space, Typography } from "antd";
import { ClockCircleOutlined } from "@ant-design/icons";
import React, { useState, useEffect } from "react";
import { primaryColor } from "../../const/constants";

const { Text } = Typography;

function Clock({ startTime }) {
  const [currentTime, setCurrentTime] = useState(new Date());

  useEffect(() => {
    const interval = setInterval(() => setCurrentTime(new Date()), 1000);
    return () => clearInterval(interval);
  }, []);

  const startTimeDate = new Date(startTime);

  const elapsedTime = Math.floor(
    (currentTime.getTime() - startTimeDate.getTime()) / 1000,
  );

  const hours = Math.floor(elapsedTime / 3600);
  const minutes = Math.floor((elapsedTime % 3600) / 60);
  const seconds = elapsedTime % 60;

  return (
    <Row justify="center">
      <Space align="center">
        <ClockCircleOutlined />
        <Text type="secondary">
          <span style={{ color: primaryColor }}>
            {" "}
            {hours}ч {minutes}м {seconds}с
          </span>
        </Text>
      </Space>
    </Row>
  );
}

export default Clock;
