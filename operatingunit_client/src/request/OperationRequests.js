import {sendPostRequest} from "./Requests";
import {serverApi} from "../const/api/serverApi";
import {processResponseError} from "./ErrorProcessor";

const FORBIDDEN_MESSAGE =
    "Вы не авторизованы! Необходимо выполнить вход в учетную запись.";

export async function getInfo(startTimeString, endTimeString) {
    const body = {
        start: startTimeString,
        end: endTimeString
    }
    try {
        const response = await sendPostRequest(serverApi.operationPlan.info, body);
        return response?.data;
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
    }
}

export async function saveOperation(operation) {
    try {
        await sendPostRequest(serverApi.operationPlan.create, operation);
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
    }
}