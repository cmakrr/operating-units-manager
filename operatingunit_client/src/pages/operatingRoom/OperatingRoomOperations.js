import { Button, Input, Space, Table, Tag } from "antd";
import React, { useEffect, useRef, useState } from "react";
import { SearchOutlined } from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import { Link } from "react-router-dom";
import {
  operationStatuses,
  tableOperationsRoomLocale,
} from "../../const/constants";
import { clientApi } from "../../const/api/clientApi";
import MainHeader from "../../components/common/MainHeader";
import { compareStrings } from "../../functions/Utils";
import { getOperationsForRoom } from "../../request/Requests";

function getMedicalWorkersInfo(operation) {
  return (
    getMedicalWorkerInfo(operation.operator) +
    getMedicalWorkerInfo(operation.assistant) +
    getMedicalWorkerInfo(operation.transfusiologist)
  );
}

function getMedicalWorkerInfo(worker) {
  const result =
    worker && (worker.fullName || worker.role)
      ? worker.fullName || worker.role
      : "";
  return result + "\n";
}

function getOperationStatus(operation) {
  return !operation.operationFact?.startTime
    ? operationStatuses.notStarted
    : operation.operationFact.endTime
      ? operationStatuses.finished
      : operationStatuses.started;
}

function OperatingRoomOperations() {
  const [operations, setOperations] = useState([]);
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const [sortedInfo, setSortedInfo] = useState({});
  const searchInput = useRef(null);

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

  const columns = [
    {
      title: "Время начала",
      dataIndex: "operationStartTime",
      key: "operationStartTime",
      width: "5%",
      ...getColumnSearchProps("operationStartTime", "Время начала"),
      sorter: (a, b) =>
        a.operationStartTime.localeCompare(b.operationStartTime),
      sortOrder:
        sortedInfo.columnKey === "operationStartTime" ? sortedInfo.order : null,
    },
    {
      title: "Время окончания",
      dataIndex: "operationEndTime",
      key: "operationEndTime",
      width: "5%",
      ...getColumnSearchProps("operationEndTime", "Время окончания"),
      sorter: (a, b) => a.operationEndTime.localeCompare(b.operationEndTime),
      sortOrder:
        sortedInfo.columnKey === "operationEndTime" ? sortedInfo.order : null,
    },
    {
      title: "Пациент",
      dataIndex: "patientFullName",
      key: "patientFullName",
      width: "15%",
      ...getColumnSearchProps("patientFullName", "Пациент"),
      sorter: (a, b) => compareStrings(a?.patientFullName, b?.patientFullName),
      sortOrder:
        sortedInfo.columnKey === "patientFullName" ? sortedInfo.order : null,
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
        <Link to={clientApi.operatingRoom.operation(record.id)}>{text}</Link>
      ),
    },
    {
      title: (
        <>
          Оператор <br /> Ассистент <br /> Трансфузиолог
        </>
      ),
      dataIndex: "medicalWorkers",
      width: "15%",
      ...getColumnSearchProps("medicalWorkers", "Медицинский работник"),
      render: (medicalWorkers) => (
        <div>
          {medicalWorkers?.split("\n").map((worker, index) => (
            <div key={index}>{worker}</div>
          ))}
        </div>
      ),
    },
    {
      title: "Статус",
      key: "operationStatus",
      dataIndex: "operationStatus",
      filters: [
        {
          text: operationStatuses.notStarted,
          value: operationStatuses.notStarted,
        },
        {
          text: operationStatuses.started,
          value: operationStatuses.started,
        },
        {
          text: operationStatuses.finished,
          value: operationStatuses.finished,
        },
      ],
      onFilter: (value, record) => record.operationStatus.indexOf(value) === 0,
      render: (operationStatus) => (
        <span>
          {(() => {
            let color = "volcano";
            if (operationStatus === operationStatuses.started) {
              color = "geek blue";
            } else if (operationStatus === operationStatuses.finished) {
              color = "green";
            }
            return (
              <Tag color={color} key={operationStatus}>
                {operationStatus?.toUpperCase()}
              </Tag>
            );
          })()}
        </span>
      ),
    },
  ];

  useEffect(() => {
    async function fetchData() {
      try {
        const operationsData = await getOperationsForRoom();
        const updatedOperationsData = operationsData?.map((operation) => ({
          ...operation,
          patientFullName: operation.patient.fullName,
          operationStartTime: operation.operationPlan.startTime.split("T")[1],
          operationEndTime: operation.operationPlan.endTime.split("T")[1],
          medicalWorkers: getMedicalWorkersInfo(operation.operationPlan),
          operationStatus: getOperationStatus(operation),
        }));
        setOperations(updatedOperationsData);
      } catch (error) {
        console.error("Произошла ошибка:", error);
      }
    }

    fetchData();
  }, []);

  return (
    <MainHeader>
      <Table
        locale={tableOperationsRoomLocale}
        columns={columns}
        dataSource={operations}
        onChange={handleChange}
      />
    </MainHeader>
  );
}

export default OperatingRoomOperations;
