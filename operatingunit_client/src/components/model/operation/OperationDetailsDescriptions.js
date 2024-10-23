import { editableOperationInfo } from "./OperationDetails";
import { getMedicalWorkerFactInfo } from "../../../functions/MedicalWorker";
import {
  CustomDescriptions,
  CustomFormDescriptions,
} from "../../common/CustomDescriptions";

export const OperationDetailsDescriptions = (operation, operationInfo) => {
  return CustomDescriptions(operationInfo);
};

export const StepDescriptions = (factInfo) => {
  return CustomDescriptions(factInfo);
};

export const OperationDetailsWithFactEditDescriptions = (operation) => {
  const initValues = [
    {
      name: ["instruments"],
      value: operation?.operationFact?.instruments
        ? operation?.operationFact?.instruments
        : operation.instruments,
    },
    {
      name: ["assistantName"],
      value: getMedicalWorkerFactInfo(
        operation?.operationPlan?.assistant,
        operation?.operationFact?.assistant,
      ),
    },
    {
      name: ["operatorName"],
      value: getMedicalWorkerFactInfo(
        operation?.operationPlan?.operator,
        operation?.operationFact?.operator,
      ),
    },
    {
      name: ["transfusiologistName"],
      value: getMedicalWorkerFactInfo(
        operation?.operationPlan?.transfusiologist,
        operation?.operationFact?.transfusiologist,
      ),
    },
  ];

  return CustomFormDescriptions(
    operation?.id,
    operation?.operationFact?.id,
    editableOperationInfo(operation),
    {},
    {},
    initValues,
    "Приступить к операции",
  );
};
