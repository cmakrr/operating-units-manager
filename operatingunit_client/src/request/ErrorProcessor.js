import { message } from "antd";

export function processResponseError(error, forbiddenMessage) {
  console.log(error);

  const errorStatus = error?.response?.status;
  const showMessage = (msg) => message.error(<span>{msg}</span>);
  if (!errorStatus) {
    showMessage("Не удалось подключиться к серверу!");
    return;
  }

  switch (true) {
    case errorStatus === 403:
      showMessage(forbiddenMessage);
      break;

    case errorStatus >= 400 && errorStatus < 500:
      if (error?.response?.data?.violations) {
        showMessage(error?.response?.data?.violations[0]?.message);
      } else {
        showMessage(error?.response?.data?.error || "Неизвестная ошибка.");
      }
      break;

    case errorStatus >= 500:
      showMessage("Произошла ошибка на сервере!");
      break;

    default:
      showMessage("Произошла неизвестная ошибка.");
      break;
  }
}
