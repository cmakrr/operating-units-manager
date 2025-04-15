import {Button, Input, Space, Table} from "antd";
import {blueColor, patientsTableLocale} from "../../const/constants";
import React, {useRef, useState} from "react";
import {SearchOutlined,} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {compareNullableStrings, compareStrings} from "../../functions/Utils";
import {compareNumbers} from "../../utils/compareUtils";
import {WorkerStatus} from "../../functions/MedicalWorker";

export const WorkersTable = ({workerEntities, openModal}) => {
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const [sortedInfo, setSortedInfo] = useState({});
    const searchInput = useRef(null);

    const workers = workerEntities?.map((worker) => ({
        ...worker,
        idd: worker.id,
        status: WorkerStatus[worker.workerStatus]
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
                        icon={<SearchOutlined/>}
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

    const workerColumns = [
        {
            title: "Идентификационный номер",
            dataIndex: "id",
            key: "id",
            width: "10%",
            ...getColumnSearchProps("id", "Идентификационный номер"),
            sorter: (a, b) => a.id > b.id ? 1 : -1,
            sortOrder: sortedInfo.columnKey === "id" ? sortedInfo.order : null,
        },
        {
            title: "ФИО",
            dataIndex: "fullName",
            key: "fullName",
            ...getColumnSearchProps("fullName", "ФИО"),
            sorter: (a, b) => compareStrings(a?.fullName, b?.fullName),
            sortOrder: sortedInfo.columnKey === "fullName" ? sortedInfo.order : null,
        },
        {
            title: "Должность",
            key: "position",
            dataIndex: "position",
            ...getColumnSearchProps("position", "должность"),
            sorter: (a, b) => compareNullableStrings(a.position, b.position),
            sortOrder: sortedInfo.columnKey === "position" ? sortedInfo.order : null,
        },
        {
            title: "Статус",
            dataIndex: "status",
            key: "status",
            ...getColumnSearchProps("status", "статус"),
            sorter: (a, b) => compareNumbers(a.workerStatus, b.workerStatus),
            sortOrder:
                sortedInfo.columnKey === "status" ? sortedInfo.order : null,
        },
        {
            title: "Редактирование",
            dataIndex: "idd",
            key: "idd",
            render: (idd, record) => (
                <>
                    <Button
                        style={{
                            width: "100%",
                            backgroundColor: blueColor,
                            color: "white",
                        }}
                        onClick={() => {
                            openModal(idd);
                        }}
                    >
                        Редактирование
                    </Button>
                </>
            )
        }
    ];

    return (
        <Table
            locale={patientsTableLocale}
            columns={workerColumns}
            dataSource={workers}
            onChange={handleChange}
        />
    );
};

export default WorkersTable;