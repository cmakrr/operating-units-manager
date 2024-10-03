import Card from "antd/lib/card/Card";
import React from "react";

export const patientDetails = (patient) => {
  return [
    {
      label: "ФИО",
      children: (
        <Card bordered={false} size={"small"}>
          {patient?.fullName}
        </Card>
      ),
      span: 3,
    },
    {
      label: patient?.birthYear ? "Год рождения" : "Возраст",
      children: (
        <Card bordered={false} size={"small"}>
          {patient?.birthYear ? patient?.birthYear : patient?.age}
        </Card>
      ),
      span: 3,
    },
    {
      label: "Номер палаты",
      children: (
        <Card bordered={false} size={"small"}>
          {patient?.roomNumber}
        </Card>
      ),
      span: 3,
    },
  ];
};
