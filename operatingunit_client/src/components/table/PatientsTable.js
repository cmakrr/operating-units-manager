import {Button, Input, message, Space, Table} from "antd";
import {blueColor, patientsTableLocale} from "../../const/constants";
import React, {useRef, useState} from "react";
import {SearchOutlined,} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {compareStrings} from "../../functions/Utils";
import {clientApi} from "../../const/api/clientApi";
import {dischargePatient} from "../../request/PatientRequests";

async function dischargePatientById(id) {
    dischargePatient(id);

    message.success(<span>{`Пациент был выписан успешно!`}</span>);

    setTimeout(() => {
        window.location.replace(clientApi.manager.patients);
    }, 1200);
}

export const PatientsTable = ({patients}) => {
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const [sortedInfo, setSortedInfo] = useState({});
    const searchInput = useRef(null);

    patients = patients?.map((patient) => ({
        ...patient,
        idd: patient.id,
        fullName: patient.fullName,
        birthYear: patient.birthYear,
        roomNumber: patient.roomNumber,
        description: patient.description
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

    const patientColumns = [
        {
            title: "ФИО",
            dataIndex: "name",
            key: "name",
            ...getColumnSearchProps("name", "ФИО"),
            sorter: (a, b) => compareStrings(a?.name, b?.name),
            sortOrder: sortedInfo.columnKey === "name" ? sortedInfo.order : null,
        },
        {
            title: "Дата рождения",
            key: "birthDay",
            dataIndex: "birthDay",
            ...getColumnSearchProps("birthDay", "Дата рождения"),
            sorter: (a, b) => a?.birthDay.localeCompare(b?.birthDay),
            sortOrder: sortedInfo.columnKey === "birthDay" ? sortedInfo.order : null,
        },
        {
            title: "Палата",
            dataIndex: "room",
            key: "room",
            ...getColumnSearchProps("room", "Палата"),
            sorter: (a, b) => compareStrings(a?.room, b?.room),
            sortOrder:
                sortedInfo.columnKey === "room" ? sortedInfo.order : null,
        },
        {
            title: "Описание",
            dataIndex: "description",
            key: "description",
        },
        {
            title: "Управление",
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
                            dischargePatientById(idd);
                        }}
                    >
                        Выписать
                    </Button>
                </>
            )
        }
    ];

    return (
        <Table
            locale={patientsTableLocale}
            columns={patientColumns}
            dataSource={patients}
            onChange={handleChange}
        />
    );
};

export default PatientsTable;