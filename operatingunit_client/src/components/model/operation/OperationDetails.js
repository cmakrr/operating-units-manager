import Card from "antd/lib/card/Card";
import { Button, Col, Descriptions, Form, Row, Tag } from "antd";
import {
  operationStatuses,
  operationStepCancelStatus,
  primaryColor,
} from "../../../const/constants";
import Title from "antd/lib/typography/Title";
import {
  OperationPlanEditableFactCards,
  OperationPlanFactCards,
} from "../../card/Cards";
import { getMedicalWorkerInfo } from "../../../functions/MedicalWorker";
import { patientDetails } from "../patient/PatientDetails";
import { Link } from "react-router-dom";
import { clientApi } from "../../../const/api/clientApi";
import {
  cancelOperationFactStart,
  createOperationFact,
  finishOperationFact,
  returnToPreviousOperationFactStep,
  startNextOperationFactStep,
  updateOperationFactStepAndStartNext,
} from "../../../request/Requests";
import OperationSteps from "./OperationSteps";
import React from "react";
import CurrentClock from "../../common/CurrentClock";
import { getOperationReport } from "../../../request/ReportRequests";

const immutableOperationInfo = (operation) => [
  {
    label: "Название операции",
    children: <Card bordered={false}>{operation?.operationName}</Card>,
    span: 3,
  },
  {
    label: "Пациент",
    children: (
      <Descriptions
        bordered
        items={patientDetails(operation?.patient)}
        size={"small"}
        labelStyle={{
          color: "black",
          backgroundColor: "#dfede2",
        }}
      />
    ),
    span: 3,
  },
  {
    children: (
      <Row gutter={16}>
        <Col span={12}>
          <Card
            bordered={false}
            style={{ background: primaryColor }}
            size={"small"}
            title={
              <Title level={4} strong style={{ color: "white" }}>
                ОПЕРАЦИОННЫЙ ПЛАН
              </Title>
            }
          ></Card>
        </Col>
        <Col span={12}>
          <Card
            bordered={false}
            style={{ background: primaryColor }}
            size={"small"}
            title={
              <Title level={4} strong style={{ color: "white" }}>
                ОПЕРАЦИОННЫЙ ФАКТ
              </Title>
            }
          ></Card>
        </Col>
      </Row>
    ),
    span: 3,
  },
  {
    label: "Время начала",
    children: OperationPlanFactCards(
      operation?.operationPlan?.startTime?.replace("T", " ")?.split(`.`)[0],
      operationStatuses.notStarted,
      operation?.operationFact?.startTime?.replace("T", " ")?.split(`.`)[0],
      operationStatuses.notStarted,
    ),
    span: 3,
  },
  {
    label: "Время окончания",
    children: OperationPlanFactCards(
      operation?.operationPlan?.endTime?.replace("T", " ")?.split(`.`)[0],
      operationStatuses.notStarted,
      operation?.operationFact?.endTime?.replace("T", " ")?.split(`.`)[0],
      operationStatuses.notFinished,
    ),
    span: 3,
  },
];

const _operationInfo = (operation) => [
  {
    label: "Оператор",
    children: OperationPlanFactCards(
      getMedicalWorkerInfo(operation?.operationPlan?.operator),
      "не назначен",
      getMedicalWorkerInfo(operation?.operationFact?.operator),
      "не назначен",
    ),
    span: 3,
  },
  {
    label: "Ассистент",
    children: OperationPlanFactCards(
      getMedicalWorkerInfo(operation?.operationPlan?.assistant),
      "не назначен",
      getMedicalWorkerInfo(operation?.operationFact?.assistant),
      "не назначен",
    ),
    span: 3,
  },
  {
    label: "Трансфузиолог",
    children: OperationPlanFactCards(
      getMedicalWorkerInfo(operation?.operationPlan?.transfusiologist),
      "не назначен",
      getMedicalWorkerInfo(operation?.operationFact?.transfusiologist),
      "не назначен",
    ),
    span: 3,
  },
  {
    label: "Инструменты",
    children: OperationPlanFactCards(
      operation?.instruments,
      "не установлены",
      operation?.operationFact?.instruments,
      "не установлены",
    ),
    span: 3,
  },
];

const getValue = (value, planValue) => {
  return value ? value : planValue;
};

const _editableOperationInfo = (operation) => [
  {
    label: "Инструменты",
    children: OperationPlanEditableFactCards(
      operation?.instruments,
      "не установлены",
      getValue(operation?.operationFact?.instruments, operation?.instruments),
      "не установлены",
      "instruments",
    ),
    span: 3,
  },
  {
    label: "Ассистент",
    children: OperationPlanEditableFactCards(
      getMedicalWorkerInfo(operation?.operationPlan?.assistant),
      "не назначен",
      getValue(
        getMedicalWorkerInfo(operation?.operationFact?.assistant),
        getMedicalWorkerInfo(operation?.operationPlan?.assistant),
      ),
      "не назначен",
      "assistantName",
    ),
    span: 3,
  },
  {
    label: "Оператор",
    children: OperationPlanEditableFactCards(
      getMedicalWorkerInfo(operation?.operationPlan?.operator),
      "не назначен",
      getValue(
        getMedicalWorkerInfo(operation?.operationFact?.operator),
        getMedicalWorkerInfo(operation?.operationPlan?.operator),
      ),
      "не назначен",
      "operatorName",
    ),
    span: 3,
  },
  {
    label: "Трансфузиолог",
    children: OperationPlanEditableFactCards(
      getMedicalWorkerInfo(operation?.operationPlan?.transfusiologist),
      "не назначен",
      getValue(
        getMedicalWorkerInfo(operation?.operationFact?.transfusiologist),
        getMedicalWorkerInfo(operation?.operationPlan?.transfusiologist),
      ),
      "не назначен",
      "transfusiologistName",
    ),
    span: 3,
  },
];

export const _currentStepDetails = (id, operationFact, nextStep) => {
  const lastStep = getPrevStep(operationFact?.steps);
  let arr = [];

  if (operationFact?.currentStep?.startTime) {
    arr = arr.concat({
      label: "Продолжительность",
      children: (
        <CurrentClock startTime={operationFact?.currentStep?.startTime} />
      ),
      span: 3,
    });
  }
  return arr.concat(
    operationFact?.currentStep?.step
      ? _currentStepButton(
          id,
          operationFact?.id,
          operationFact?.currentStep,
          nextStep?.step?.name,
        )
      : _finishOperationButton(
          id,
          operationFact?.id,
          operationFact?.currentStep,
          lastStep,
        ),
  );
};

const _currentStepInfo = (id, operationFact, nextStep) => {
  return [
    {
      label: operationFact?.currentStep?.step?.name,
      children: (
        <Descriptions
          bordered
          items={_currentStepDetails(id, operationFact, nextStep)}
          size={"small"}
          labelStyle={{
            color: "black",
            backgroundColor: "#dfede2",
          }}
        />
      ),
      span: 3,
    },
  ];
};

const _formButton = (id) => {
  return [
    {
      label: (
        <Button
          style={{
            width: "100%",
            backgroundColor: "#4096ff",
            color: "white",
          }}
          onClick={() =>
            (window.location.href = clientApi.operatingRoom.operation(id))
          }
        >
          Назад
        </Button>
      ),
      children: (
        <Form.Item type="primary" style={{ margin: "auto" }}>
          <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
            Сохранить и начать операцию
          </Button>
        </Form.Item>
      ),
      span: 3,
    },
  ];
};

const handleCreateOperationButtonClick = async (id) => {
  try {
    const result = await createOperationFact(id);
    if (result) {
      window.location.assign(
        clientApi.operatingRoom.editOperationFact(id, result.id),
      );
    } else {
    }
  } catch (error) {
    console.error("Произошла ошибка при получении операции:", error);
  }
};

const handleCreateOperationReportButtonClick = async (id) => {
  try {
    await getOperationReport(id);
  } catch (error) {
    console.error("Произошла ошибка при получении отчета:", error);
  }
};

const _createOperationFactButton = (id) => {
  return [
    {
      label: (
        <Link to={clientApi.operatingRoom.operations}>
          <Button
            style={{
              width: "100%",
              backgroundColor: "#4096ff",
              color: "white",
            }}
          >
            Назад
          </Button>
        </Link>
      ),
      children: (
        <Button
          type="primary"
          htmlType="submit"
          style={{ width: "100%" }}
          onClick={() => handleCreateOperationButtonClick(id)}
        >
          Приступить к операции
        </Button>
      ),
      span: 3,
    },
  ];
};

const _stepsButton = (id, factId, nextStepName) => {
  return [
    {
      label: (
        <Button
          style={{
            width: "100%",
            backgroundColor: "#4096ff",
            color: "white",
          }}
          onClick={() =>
            cancelOperationFactStart(
              id,
              factId,
              clientApi.operatingRoom.operation(id),
            )
          }
        >
          Отменить начало операции
        </Button>
      ),
      children: (
        <Button
          type="primary"
          htmlType="submit"
          style={{ width: "100%" }}
          onClick={() => startNextOperationFactStep(id, factId)}
        >
          Начать "{nextStepName}"
        </Button>
      ),
      span: 3,
    },
  ];
};

const _finishOperationButton = (id, factId, currentStep, lastStep) => {
  return [
    {
      label: currentStep?.canCancelled ? (
        <Button
          style={{
            width: "100%",
            backgroundColor: "#4096ff",
            color: "white",
          }}
          onClick={() =>
            returnToPreviousOperationFactStep(id, factId, lastStep)
          }
        >
          Отменить "Конец операции"
        </Button>
      ) : (
        <Tag color={"volcano"} key={operationStepCancelStatus.cantCanceled}>
          {operationStepCancelStatus.cantCanceled?.toUpperCase()}
        </Tag>
      ),
      children: (
        <Button
          type="primary"
          htmlType="submit"
          style={{ width: "100%" }}
          onClick={() => finishOperationFact(id, factId)}
        >
          Закончить операцию
        </Button>
      ),
      span: 3,
    },
  ];
};

const _currentStepButton = (id, factId, currentStep, nextStepName) => {
  return [
    {
      label: currentStep?.canCancelled ? (
        <Button
          style={{
            width: "100%",
            backgroundColor: "#4096ff",
            color: "white",
          }}
          onClick={() =>
            returnToPreviousOperationFactStep(
              id,
              factId,
              currentStep,
              currentStep?.step?.stepNumber === 1,
            )
          }
        >
          Отменить шаг "{currentStep?.step?.name}"
        </Button>
      ) : (
        <Tag color={"volcano"} key={operationStepCancelStatus.cantCanceled}>
          {operationStepCancelStatus?.cantCanceled?.toUpperCase()}
        </Tag>
      ),
      children: (
        <Button
          type="primary"
          htmlType="submit"
          style={{ width: "100%" }}
          onClick={() =>
            updateOperationFactStepAndStartNext(id, factId, currentStep)
          }
        >
          {nextStepName
            ? `Начать "${nextStepName}"`
            : `Закончить "${currentStep?.step?.name}"`}
        </Button>
      ),
      span: 3,
    },
  ];
};

const _createOperationReportButton = (id, canGenerateReport) => {
  return [
    {
      label: (
        <Button
          style={{
            width: "100%",
            backgroundColor: "#4096ff",
            color: "white",
          }}
          onClick={() => window.history.back()}
        >
          Назад
        </Button>
      ),
      children: canGenerateReport ? (
        <Button
          type="primary"
          htmlType="submit"
          style={{ width: "100%" }}
          onClick={() => handleCreateOperationReportButtonClick(id)}
        >
          Сформировать операционный отчет
        </Button>
      ) : (
        ``
      ),
      span: 3,
    },
  ];
};

const _operationListButton = () => {
  return [
    {
      label: "",
      children: (
        <Button
          style={{
            width: "100%",
            backgroundColor: "#4096ff",
            color: "white",
          }}
          onClick={() =>
            (window.location.href = clientApi.operatingRoom.operations)
          }
        >
          Список операций
        </Button>
      ),
      span: 3,
    },
  ];
};

export const operationInfo = (operation, isManagement) => {
  if (operation?.operationFact?.endTime || isManagement) {
    let reportButton = isManagement
      ? _createOperationReportButton(
          operation?.id,
          operation?.operationFact?.startTime,
        )
      : [];
    let mass = !isManagement ? _operationListButton() : reportButton;
    return mass.concat(
      _operationSteps(
        operation?.operationFact?.steps,
        operation?.operationFact?.startTime,
        operation?.operationFact?.currentStep?.step?.stepNumber,
        operation?.operationFact,
        false,
      ),
      immutableOperationInfo(operation),
      _operationInfo(operation, isManagement),
      mass,
    );
  }
  return [].concat(
    _createOperationFactButton(operation?.id),
    immutableOperationInfo(operation),
    _operationInfo(operation),
    _createOperationFactButton(operation?.id),
  );
};

export const editableOperationInfo = (operation) => {
  return [].concat(
    _formButton(operation?.id),
    immutableOperationInfo(operation),
    _editableOperationInfo(operation),
    _formButton(operation?.id),
  );
};

const _operationSteps = (
  steps,
  startTime,
  currentStepIndex,
  operationFact,
  canModify,
) => {
  return [
    {
      label: "Операционные шаги",
      children: OperationSteps(
        steps,
        startTime,
        currentStepIndex,
        operationFact,
        canModify,
      ),
      span: 3,
    },
  ];
};

export const operationCheckoutInfo = (operation) => {
  const stepButton = _stepsButton(
    operation?.id,
    operation?.operationFact?.id,
    operation?.operationFact?.steps[0]?.step?.name,
  );

  return [].concat(
    stepButton,
    _operationSteps(
      operation?.operationFact?.steps,
      operation?.operationFact?.startTime,
      0,
      operation?.operationFact,
      false,
    ),
    stepButton,
    immutableOperationInfo(operation),
    _operationInfo(operation),
  );
};

function getPrevStep(steps) {
  const sortedSteps = steps?.sort(
    (a, b) => b?.step?.stepNumber - a?.step?.stepNumber,
  );
  if (sortedSteps && sortedSteps[0]) {
    return sortedSteps[0];
  }
}

function getNextStep(steps, targetStepNumber) {
  if (!targetStepNumber) {
    const sortedSteps = steps?.sort(
      (a, b) => b?.step?.stepNumber - a?.step?.stepNumber,
    );
    if (sortedSteps && sortedSteps[0]) {
      const st = {
        step: { stepNumber: sortedSteps[0]?.step?.stepNumber + 1 },
      };
      return st;
    }
  }

  const filteredSteps = steps?.filter(
    (step) => step?.step?.stepNumber > targetStepNumber,
  );
  if (filteredSteps?.length === 0) {
    return null;
  }

  const sortedSteps = filteredSteps?.sort(
    (a, b) => a?.step?.stepNumber - b?.step?.stepNumber,
  );
  if (sortedSteps && sortedSteps[0]) {
    return sortedSteps[0];
  }
}

export const stepInfo = (id, operationFact) => {
  const nextStep = getNextStep(
    operationFact?.steps,
    operationFact?.currentStep?.step?.stepNumber,
  );

  return [].concat(
    _currentStepInfo(id, operationFact, nextStep),
    _operationSteps(
      operationFact?.steps,
      operationFact?.startTime,
      operationFact?.currentStep.step?.stepNumber,
      operationFact,
      true,
    ),
    _currentStepInfo(id, operationFact, nextStep),
  );
};

export const checkoutStepInfo = (id, operationFact) => {
  return [].concat(
    _currentStepInfo(id, operationFact, null),

    _operationSteps(
      operationFact?.steps,
      operationFact?.startTime,
      null,
      operationFact,
      false,
    ),
    _currentStepInfo(id, operationFact, null),
  );
};
