import {serverApi} from "../const/api/serverApi";
import {sendPostRequest} from "./Requests";
import {processResponseError} from "./ErrorProcessor";

const FORBIDDEN_MESSAGE =
    "Вы не авторизованы! Необходимо выполнить вход в учетную запись.";

export async function getWorkersAnalysis(id, dateRange) {
    try {
        const response = await sendPostRequest(serverApi.analysis.workers(id), dateRange);
        return response?.data;
    } catch (error) {
        processResponseError(error,`Информация для данного работника не найдена!`);
    }
}

export async function getRoomsAnalysis(name, dateRange) {
    try {
        const response = await sendPostRequest(serverApi.analysis.rooms(name), dateRange);
        return response?.data;
    } catch (error) {
        processResponseError(error, `Информация для данной операционной не найдена!`);
    }
}

export async function getAnalysis(dateRange) {
    try {
        const response = await sendPostRequest(serverApi.analysis.operations, dateRange);
        return response?.data;
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
    }
}