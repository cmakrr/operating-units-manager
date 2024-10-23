import { serverApi } from "../const/api/serverApi";
import { clientApi } from "../const/api/clientApi";
import {sendDeleteRequest, sendGetRequest, sendPostRequest} from "./Requests";
import { processResponseError } from "./ErrorProcessor";
import { message } from "antd";
import axios from "axios";

const FORBIDDEN_MESSAGE =
  "Вы не авторизованы! Необходимо выполнить вход в учетную запись.";

export async function createOperatingRoom(values) {
  try {
    if (values?.ipAddress === "") {
      values.ipAddress = null;
    }
    const response = await sendPostRequest(
      serverApi.operatingRoom.create,
      values,
    );
    message.success(
      <span>{`Операционный блок ${response?.data?.name} добавлен успешно`}</span>,
    );

    setTimeout(() => {
      window.location.assign(clientApi.manager.operatingRooms);
    }, 1200);
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}

export async function getOperatingRooms() {
  try {
    const rooms = await sendGetRequest(serverApi.operatingRoom.rooms);

    return rooms?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return [];
  }
}

export async function getOperatingRoomsForMonitoring() {
  try {
    const operations = await sendGetRequest(serverApi.operatingRoom.monitoring);

    return operations?.data;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return [];
  }
}

export async function deleteOperatingRoom(id) {
  try {
    await sendDeleteRequest(serverApi.operatingRoom.room(id));

    message.success(<span>{`Операционный блок удален успешно!`}</span>);
    setTimeout(() => {
      window.location.replace(clientApi.manager.operatingRooms);
    }, 1200);
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}

export async function updateOperatingRoomIp(id, ip) {
  try {
    const url = serverApi.operatingRoom.ip(id);
    if (ip) {
      await axios.put(url, ip, {
        headers: {
          "Content-Type": "text/plain",
        },
      });
    } else {
      await sendDeleteRequest(url)
    }

    message.success(<span>{`IP-адрес обновлен успешно!`}</span>);
    setTimeout(() => {
      window.location.replace(clientApi.manager.operatingRooms);
    }, 1200);
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}

export async function getOperatingRoomName() {
  try {
    const response = await sendGetRequest(serverApi.operatingRoom.name);
    return response?.data?.operatingRoomName;
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
    return null;
  }
}
