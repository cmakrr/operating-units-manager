import { tokenKey } from "../const/constants";
import { processResponseError } from "./ErrorProcessor";
import { sendDeleteRequest, sendGetRequest, sendPostRequest } from "./Requests";
import { serverApi } from "../const/api/serverApi";
import { clientApi } from "../const/api/clientApi";
import { message } from "antd";
import axios from "axios";

const FORBIDDEN_MESSAGE =
  "Вы не авторизованы! Необходимо выполнить вход в учетную запись.";

export async function signIn(url, values, redirectUrl) {
  try {
    const response = await sendPostRequest(url, values);
    sessionStorage.setItem(tokenKey, response.data.token);

    window.location.assign(redirectUrl);
  } catch (error) {
    processResponseError(error, "Введен неверный логин или пароль!");
  }
}

export async function createUser(values) {
  try {
    const response = await sendPostRequest(serverApi.users.create, values);
    message.success(
      <span>{`Пользователь ${response?.data?.login} зарегистрирован!`}</span>,
    );

    setTimeout(() => {
      window.location.assign(
        clientApi.manager.users(values?.roleType?.toLowerCase()),
      );
    }, 1200);
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}

export async function getUsersByRole(role) {
  try {
    const users = await sendGetRequest(serverApi.users.getByRole(role));
    return users?.data;
  } catch (error) {
    console.error("Произошла ошибка:", error);
    return [];
  }
}

export async function updateUserPassword(id, password) {
  try {
    await axios.put(serverApi.users.userById(id), password, {
      headers: {
        "Content-Type": "text/plain",
      },
    });

    message.success(<span>{`Пароль обновлен успешно!`}</span>);
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}

export async function deleteUser(id, role) {
  try {
    await sendDeleteRequest(serverApi.users.userById(id));

    message.success(<span>{`Пользователь удален успешно!`}</span>);
    setTimeout(() => {
      window.location.replace(clientApi.manager.users(role.toLowerCase()));
    }, 1200);
  } catch (error) {
    processResponseError(error, FORBIDDEN_MESSAGE);
  }
}
