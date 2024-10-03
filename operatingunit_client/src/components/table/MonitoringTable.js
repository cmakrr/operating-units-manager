import { Button, Input, Space, Table, Tag } from "antd";
import { monitoringTableLocale, primaryColor } from "../../const/constants";
import React, { useRef, useState } from "react";
import { Link } from "react-router-dom";
import { clientApi } from "../../const/api/clientApi";
import {
  CheckCircleTwoTone,
  ExclamationCircleTwoTone,
  SearchOutlined,
  SyncOutlined,
} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import { compareStrings } from "../../functions/Utils";

function isOutOfTime(planEndTime) {
  if (!planEndTime) {
    return null;
  } else {
    const endTime = new Date(planEndTime);
    const currentTime = new Date();

    return endTime > currentTime;
  }
}

function getOperationStep(operationFact) {
  if (operationFact) {
    if (operationFact?.currentStep?.step) {
      return operationFact?.currentStep?.step?.name;
    } else if (
      operationFact?.steps.some((step) => step?.status === "FINISHED")
    ) {
      return `Конец операции`;
    } else {
      return `Старт операции`;
    }
  } else {
    return null;
  }
}

export const MonitoringTable = ({ rooms }) => {
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const [sortedInfo, setSortedInfo] = useState({});
  const searchInput = useRef(null);

  rooms = rooms?.map((room) => ({
    ...room,
    status: isOutOfTime(room?.currentOperation?.operationPlan?.endTime),
    operationStartTime: room?.currentOperation?.operationFact?.startTime
      ?.split("T")[1]
      ?.split(`.`)[0],
    operationPlanStartTime:
      room?.currentOperation?.operationPlan?.startTime?.split("T")[1],
    operationPlanEndTime:
      room?.currentOperation?.operationPlan?.endTime?.split("T")[1],
    operationName: room?.currentOperation?.operationName,
    operationStep: getOperationStep(room?.currentOperation?.operationFact),
  }));
  const handleSearch = (selectedKeys, confirm, dataIndex) => {
    confirm();
    setSearchText(selectedKeys[0]);
    setSearchedColumn(dataIndex);
  };
  const handleReset = (clearFilters) => {
    clearFilters();
    setSearchText("");
  };
  const handleChange = (pagination, filters, sorter) => {
    setSortedInfo(sorter);
  };

  const getColumnSearchProps = (dataIndex, dataName) => ({
    filterDropdown: ({
      setSelectedKeys,
      selectedKeys,
      confirm,
      clearFilters,
      close,
    }) => (
      <div
        style={{
          padding: 8,
        }}
        onKeyDown={(e) => e.stopPropagation()}
      >
        <Input
          ref={searchInput}
          placeholder={`Поиск по ${dataName}`}
          value={selectedKeys[0]}
          onChange={(e) =>
            setSelectedKeys(e.target.value ? [e.target.value] : [])
          }
          onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
          style={{
            marginBottom: 8,
            display: "block",
          }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
            icon={<SearchOutlined />}
            size="small"
            style={{
              width: 90,
            }}
          >
            Поиск
          </Button>
          <Button
            onClick={() =>
              clearFilters &&
              handleReset(clearFilters) &&
              handleSearch(selectedKeys, confirm, dataIndex)
            }
            size="small"
            style={{
              width: 90,
            }}
          >
            Очистить
          </Button>
          <Button
            type="primary"
            danger
            size="small"
            onClick={() => {
              close();
            }}
          >
            Закрыть
          </Button>
        </Space>
      </div>
    ),
    filterIcon: (filtered) => (
      <SearchOutlined
        style={{
          color: filtered ? "#1677ff" : undefined,
        }}
      />
    ),
    onFilter: (value, record) =>
      record[dataIndex].toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: (visible) => {
      if (visible) {
        setTimeout(() => searchInput.current?.select(), 100);
      }
    },
    render: (text) =>
      searchedColumn === dataIndex ? (
        <Highlighter
          highlightStyle={{
            backgroundColor: "#ffc069",
            padding: 0,
          }}
          searchWords={[searchText]}
          autoEscape
          textToHighlight={text ? text.toString() : ""}
        />
      ) : (
        text
      ),
  });

  const monitoringColumns = [
    {
      title: "Операционный блок",
      dataIndex: "name",
      key: "name",
      ...getColumnSearchProps("name", "Операционный блок"),
      sorter: (a, b) => compareStrings(a?.name, b?.name),
      sortOrder: sortedInfo.columnKey === "name" ? sortedInfo.order : null,
      render: (name, record) =>
        record.currentOperation ? (
          <Tag icon={<SyncOutlined spin />} color="processing">
            {name}
          </Tag>
        ) : (
          <Tag color="success">{name}</Tag>
        ),
    },
    {
      title: "Статус операции",
      width: "5%",
      key: "status",
      dataIndex: "status",
      render: (status) =>
        status === false ? (
          <ExclamationCircleTwoTone twoToneColor="#fa541c" />
        ) : status === true ? (
          <CheckCircleTwoTone twoToneColor={primaryColor} />
        ) : (
          ``
        ),
    },
    {
      title: "Время начала",
      dataIndex: "operationStartTime",
      key: "operationStartTime",
      width: "5%",
      ...getColumnSearchProps("operationStartTime", "Время начала"),
      sorter: (a, b) =>
        a?.operationStartTime?.localeCompare(b?.operationStartTime),
      sortOrder:
        sortedInfo.columnKey === "operationStartTime" ? sortedInfo.order : null,
    },
    {
      title: "Планируемое время начала",
      dataIndex: "operationPlanStartTime",
      key: "operationPlanStartTime",
      width: "5%",
      ...getColumnSearchProps(
        "operationPlanStartTime",
        "Планируемое время начала",
      ),
      sorter: (a, b) =>
        a?.operationPlanStartTime?.localeCompare(b?.operationPlanStartTime),
      sortOrder:
        sortedInfo.columnKey === "operationPlanStartTime"
          ? sortedInfo.order
          : null,
    },
    {
      title: "Планируемое время окончания",
      dataIndex: "operationPlanEndTime",
      key: "operationPlanEndTime",
      width: "5%",
      ...getColumnSearchProps(
        "operationPlanEndTime",
        "Планируемое время окончания",
      ),
      sorter: (a, b) =>
        a?.operationPlanEndTime?.localeCompare(b?.operationPlanEndTime),
      sortOrder:
        sortedInfo.columnKey === "operationPlanEndTime"
          ? sortedInfo.order
          : null,
    },
    {
      title: "Название операции",
      dataIndex: "operationName",
      key: "operationName",
      width: "40%",
      ...getColumnSearchProps("operationName", "Название операции"),
      sorter: (a, b) => compareStrings(a?.operationName, b?.operationName),
      sortOrder:
        sortedInfo.columnKey === "operationName" ? sortedInfo.order : null,
      render: (text, record) => (
        <Link to={clientApi.manager.operation(record.currentOperation?.id)}>
          {text}
        </Link>
      ),
    },
    {
      title: "Этап операции",
      key: "operationStep",
      dataIndex: "operationStep",
      render: (operationStep) =>
        operationStep ? (
          <Tag color="cyan" key={operationStep}>
            {operationStep?.toUpperCase()}
          </Tag>
        ) : (
          ``
        ),
    },
  ];

  return (
    <Table
      locale={monitoringTableLocale}
      columns={monitoringColumns}
      dataSource={rooms}
      onChange={handleChange}
    />
  );
};

export default MonitoringTable;
