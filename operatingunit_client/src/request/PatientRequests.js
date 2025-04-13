import {serverApi} from "../const/api/serverApi";
import {sendGetRequest, sendPutRequest} from "./Requests";
import {processResponseError} from "./ErrorProcessor";

const FORBIDDEN_MESSAGE =
    "Вы не авторизованы! Необходимо выполнить вход в учетную запись.";

export async function dischargePatient(id) {
    try {
        await sendPutRequest(serverApi.patient.dispatchById(id));
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
    }
}

export async function getPatients() {
    try {
        const response = await sendGetRequest(serverApi.patient.getAllInHospital);
        return response?.data;
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
        return null;
    }
}
