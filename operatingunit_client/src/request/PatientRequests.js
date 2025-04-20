import {serverApi} from "../const/api/serverApi";
import {sendGetRequest, sendPostRequest, sendPutRequest} from "./Requests";
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

export async function updatePatient(patient) {
    try {
        await sendPutRequest(serverApi.patient.update, patient);
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
    }
}

export async function savePatient(patient) {
    try {
        console.log(patient);
        await sendPostRequest(serverApi.patient.save, patient);
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
    }
}

export const PatientStatus = {
    "IN_HOSPITAL": "В учреждении",
    "OUT_OF_HOSPITAL": "Выписан"
}