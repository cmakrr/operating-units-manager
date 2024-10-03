import { serverApi } from "../const/api/serverApi";
import { clientApi } from "../const/api/clientApi";
import axios from "axios";
import { processResponseError } from "./ErrorProcessor";

const FORBIDDEN_MESSAGE =
  "У вас недостаточно прав для выполнения операции или необходимо выполнить вход в учетную запись.";

export async function sendPostRequest(url, formData) {
  return await axios.post(url, formData);
}

export async function sendGetRequest(url) {
  return await axios.get(url);
}

export async function sendDeleteRequest(url) {
  return await axios.delete(url);
}

export async function sendPutRequest(url, data, headers = {}) {
  return await axios.put(url, data, { headers });
}

export async function getOperationById(operationId, isManagement) {
  try {
    const response = await sendGetRequest(
      isManagement
        ? serverApi.operations.get(operationId)
        : serverApi.tracker.operation(operationId),
    );

    return response?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function createOperationFact(operationId) {
  try {
    const response = await sendPostRequest(
      serverApi.tracker.createOperationFact(operationId),
    );

    return response?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function updateAndStartOperationFact(id, factId, values) {
  console.log(values);
  try {
    const updateResponse = await updateOperationFact(
      serverApi.tracker.updateOperationFactInfo(id, factId),
      values,
    );
    if (updateResponse) {
      const startResponse = await startOperationFact(
        serverApi.tracker.startOperationFact(id, updateResponse?.id),
      );
      if (startResponse) {
        window.location.replace(
          clientApi.operatingRoom.operationFactCheckout(id, startResponse?.id),
        );
      }
    }
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}

export async function updateOperationFact(url, values) {
  try {
    const response = await sendPutRequest(url, values);

    return response?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function startOperationFact(url) {
  try {
    const operationFact = await sendPutRequest(url, {});

    return operationFact?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function cancelOperationFactStart(id, factId, redirectUrl) {
  try {
    const operationFact = await sendPutRequest(
      serverApi.tracker.cancelOperationFactStart(id, factId),
      {},
    );
    window.location.replace(redirectUrl);

    return operationFact?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function returnToPreviousOperationFactStep(
  id,
  factId,
  step,
  toCheckout,
) {
  try {
    const cancelResponse = await cancelFinishOperationFactStep(
      id,
      factId,
      step?.step?.id,
    );
    if (cancelResponse) {
      const currentStep = await getCurrentOperationFactStep(id, factId);
      if (currentStep) {
        window.location.replace(
          !toCheckout
            ? clientApi.operatingRoom.operationStep(
                id,
                factId,
                currentStep?.step?.name,
              )
            : clientApi.operatingRoom.operationFactCheckout(id, factId),
        );
      }
    }
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}

export async function finishOperationFact(id, factId) {
  try {
    const step = await sendPutRequest(
      serverApi.tracker.finishOperationFact(id, factId),
      {},
    );
    window.location.replace(clientApi.operatingRoom.operation(id));

    return step?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function cancelFinishOperationFactStep(id, factId, stepId) {
  try {
    const step = await sendPutRequest(
      serverApi.tracker.cancelFinishOperationFactStep(id, factId, stepId),
      {},
    );

    return step?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function getCurrentOperationFactStep(id, factId) {
  try {
    const step = await sendGetRequest(
      serverApi.tracker.getCurrentOperationFactStep(id, factId),
    );

    return step?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function updateOperationFactStepAndStartNext(id, factId, step) {
  try {
    const updateResponse = await updateOperationFactStep(
      id,
      factId,
      step?.step?.id,
    );
    if (updateResponse) {
      const startResponse = await _startNextOperationFactStep(id, factId);
      if (startResponse) {
        window.location.replace(
          clientApi.operatingRoom.operationStep(
            id,
            factId,
            startResponse?.date?.currentStep?.step?.name
              ? startResponse?.date?.currentStep?.step?.name
              : "Завершение",
          ),
        );
      }
    }
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function updateOperationFactStep(id, factId, stepId) {
  const inputValue = document.getElementById("commentForCurrentStep").value;
  const data = { comment: inputValue };
  try {
    const stepData = await sendPutRequest(
      serverApi.tracker.updateOperationFactStep(id, factId, stepId),
      data,
    );

    return stepData?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

async function _startNextOperationFactStep(id, factId) {
  try {
    const stepData = await sendPostRequest(
      serverApi.tracker.startNextOperationFactStep(id, factId),
    );

    return stepData?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
  return {};
}

export async function startNextOperationFactStep(id, factId) {
  try {
    const stepData = await sendPostRequest(
      serverApi.tracker.startNextOperationFactStep(id, factId),
    );
    if (stepData) {
      const step = stepData.data;
      window.location.replace(
        clientApi.operatingRoom.operationStep(id, factId, step?.step?.name),
      );
    }
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}

export async function getOperationFact(id, factId) {
  try {
    const operationFact = await sendGetRequest(
      serverApi.tracker.getOperationFact(id, factId),
    );

    return operationFact?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return {};
  }
}

export async function getOperationsForDate(date) {
  try {
    const operations = await sendGetRequest(
      serverApi.operations.getAllForDate(date),
    );

    return operations?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return [];
  }
}

export async function getOngoingOperations(startDate, endDate) {
  try {
    const operations = await sendGetRequest(
      serverApi.operations.getOngoingForDates(startDate, endDate),
    );

    return operations?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return [];
  }
}

export async function getOperationsForRoom() {
  try {
    const response = await sendGetRequest(serverApi.tracker.todayOperations);

    return response?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return null;
  }
}
