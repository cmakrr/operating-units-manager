import {serverApi} from "../const/api/serverApi";
import axios from "axios";
import FileDownload from "js-file-download";
import {processResponseError} from "./ErrorProcessor";


export async function getOngoingOperationReport(startDate, endDate) {
    await downloadFile(serverApi.report.ongoingOperations(startDate, endDate))
}

export async function getOperationReport(operationId) {
    await downloadFile(serverApi.report.operation(operationId))
}

async function downloadFile(url) {
    await axios({
        url: url,
        method: "GET",
        responseType: "blob",
    }).then((response) => {
        let fileName = decodeURIComponent(
            response.headers["x-suggested-filename"],
        ).replaceAll(`+`, ` `);
        FileDownload(response.data, fileName);
    }).catch(error =>
        processResponseError(error, "У вас нет прав на скачивание этого файла")
    );
}
