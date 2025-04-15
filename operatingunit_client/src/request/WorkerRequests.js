import {sendGetRequest, sendPostRequest, sendPutRequest} from "./Requests";
import {serverApi} from "../const/api/serverApi";
import {processResponseError} from "./ErrorProcessor";

const FORBIDDEN_MESSAGE =
    "Вы не авторизованы! Необходимо выполнить вход в учетную запись.";

export async function getWorkers() {
    try {
        return await sendGetRequest(serverApi.worker.getAll)?.data;
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
        return null;
    }
}

export async function updateWorker(worker) {
    try {
        await sendPutRequest(serverApi.worker.update, worker);
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
    }
}

export async function saveWorker(worker) {
    try {
        await sendPostRequest(serverApi.worker.save, worker);
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
    }
}
