import {serverApi} from "../const/api/serverApi";
import {sendPostRequest} from "./Requests";
import {processResponseError} from "./ErrorProcessor";

const FORBIDDEN_MESSAGE =
    "Вы не авторизованы! Необходимо выполнить вход в учетную запись.";

export async function getLogs(dateRange) {
    try {
        const rooms = await sendPostRequest(serverApi.logs.getBetweenDates, dateRange);

        return rooms?.data;
    } catch (error) {
        processResponseError(error, FORBIDDEN_MESSAGE);
        return [];
    }
}
