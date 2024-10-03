import React from "react";
import { InboxOutlined } from "@ant-design/icons";
import { message, Tag, Upload } from "antd";
import {
  availableOperationPlanFileFormats,
  isOperationPlanFormatAvailable,
  primaryColor,
} from "../../const/constants";
import { serverApi } from "../../const/api/serverApi";
import { getAuthHeader } from "../../functions/Token";

const { Dragger } = Upload;

const availableFormatsTags = Object.keys(availableOperationPlanFileFormats).map(
  (format) => <Tag color={primaryColor}>{format}</Tag>,
);

const props = {
  name: "planFile",
  maxCount: 1,
  beforeUpload: (file) => {
    const isAvailableFormat = isOperationPlanFormatAvailable(file.type);
    if (!isAvailableFormat) {
      message.error(
        <span>
          {file.name} не имеет поддерживаемый формат!
          <br />
          Поддерживаемые форматы: {availableFormatsTags}.
        </span>,
      );
    }
    return isAvailableFormat || Upload.LIST_IGNORE;
  },
  action: `http://127.0.0.1:8002${serverApi.operationPlan.load}`,
  headers: {
    Authorization: getAuthHeader(),
  },
  onChange(info) {
    const { status } = info.file;
    if (status === "done") {
      message.success(`${info.file.name} загрузился успешно.`);
    } else if (status === "error") {
      message.error(
        <span>
          {info.file.name} не был загружен!
          <br />
          {info.file.response.error
            ? info.file.response.error === "Internal Server Error"
              ? "Проверьте валидность структуры и данных плана!"
              : info.file.response.error
            : ""}
        </span>,
      );
    }
  },
};

function FileLoader() {
  return (
    <Dragger {...props}>
      <p className="ant-upload-drag-icon">
        <InboxOutlined />
      </p>
      <p className="ant-upload-text">
        Нажмите или перетяните операционный план для загрузки
      </p>
    </Dragger>
  );
}

export default FileLoader;
