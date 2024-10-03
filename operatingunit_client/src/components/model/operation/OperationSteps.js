import { Collapse, Steps } from "antd";
import { getOperationStepStatus, primaryColor } from "../../../const/constants";
import { Typography } from "antd";
import TextArea from "antd/es/input/TextArea";
import React from "react";

const { Text } = Typography;

const operationStep = (index, time, description, status, isFinish) => {
  return [
    {
      title: `${index} шаг`,
      subTitle: time ? (
        <Text type="secondary">
          {!isFinish && "Старт в"}
          <span style={{ color: primaryColor }}>
            {" "}
            {time?.split(`T`)[1]?.split(`.`)[0]}
          </span>
        </Text>
      ) : (
        ""
      ),
      description: description,
      status: status,
    },
  ];
};

const getSteps = (steps, startIndex, currentStepIndex, canModify) => {
  steps?.sort((a, b) => a.step.stepNumber - b.step.stepNumber);

  return steps?.map((item, index) => ({
    title: `${startIndex + index} шаг`,
    status: getOperationStepStatus(item?.status),
    subTitle: item?.startTime ? (
      <Text type="secondary">
        Старт в{" "}
        <span style={{ color: primaryColor }}>
          {" "}
          {item?.startTime?.split(`T`)[1].split(`.`)[0]}
        </span>
      </Text>
    ) : (
      ""
    ),
    description: (
      <div>
        <Text type={"secondary"}>{item?.step?.name}</Text>
        <div style={{ height: "10px" }}></div>
        {currentStepIndex === index + 1 || item?.comment ? (
          <Collapse
            collapsible="header"
            {...(currentStepIndex === index + 1 ? { activeKey: 0 } : {})}
            items={[
              {
                label: "Комментарий",
                children:
                  (item?.comment && !(currentStepIndex === index + 1)) ||
                  canModify === false ? (
                    <Text>{item?.comment}</Text>
                  ) : (
                    <TextArea
                      id={"commentForCurrentStep"}
                      autoSize
                      size={"large"}
                      style={{ marginTop: "3px" }}
                      defaultValue={item?.comment}
                    ></TextArea>
                  ),
              },
            ]}
          />
        ) : (
          " "
        )}{" "}
      </div>
    ),
  }));
};
const OperationSteps = (
  steps,
  startTime,
  currentStepIndex,
  operationFact,
  canModify,
) => {
  let index = 1;
  let lastIndex = steps?.length + index;

  const items = [].concat(
    operationStep(
      index,
      startTime ? startTime : operationFact?.startTime,
      `Старт операции`,
      !operationFact?.startTime
        ? "wait"
        : operationFact?.startTime &&
            !operationFact?.currentStep &&
            !operationFact?.endTime
          ? "process"
          : operationFact?.endTime
            ? "finish"
            : currentStepIndex === index - 1
              ? "process"
              : "finish",
    ),
    getSteps(steps, index + 1, currentStepIndex, canModify),
    operationStep(
      lastIndex ? lastIndex + 1 : 2,
      operationFact?.endTime,
      `Конец операции`,
      operationFact?.endTime
        ? "finish"
        : currentStepIndex === undefined || currentStepIndex >= 0
          ? "wait"
          : "process",
      true,
    ),
  );

  return (
    <div style={{ display: "flex", justifyContent: "center" }}>
      <Steps
        percent={60}
        direction="vertical"
        items={items}
        style={{ display: "flex", justifyContent: "center" }}
      />
    </div>
  );
};

export default OperationSteps;
