import {Button, Form, Input, message, Modal, Space, Table, Tag} from "antd";
import {blueColor, ipPattern, managerMenuItems, monitoringTableLocale,} from "../../const/constants";
import React, {useRef, useState} from "react";
import {SearchOutlined} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {deleteOperatingRoom, updateOperatingRoomIp,} from "../../request/OperatingRoomRequests";
import {getRoleFromToken} from "../../functions/Token";
import {compareStrings} from "../../functions/Utils";


export const OperatingRoomTable = ({rooms}) => {
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const [sortedInfo, setSortedInfo] = useState({});
    const searchInput = useRef(null);
    const [modalOpen, setModalOpen] = useState(false);
    const [selectedId, setSelectedId] = useState(null);
    const [selectedIp, setSelectedIp] = useState("");
    const [form] = Form.useForm();

    const userRole = getRoleFromToken();

    rooms = rooms?.map((room) => ({
        ...room,
        ipAddress: room?.ip?.address,
        idd: room?.id,
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

    let monitoringColumns = [
        {
            title: "Операционный блок",
            dataIndex: "name",
            key: "name",
            width: "5%",
            ...getColumnSearchProps("name", "Операционный блок"),
            sorter: (a, b) => compareStrings(a?.name, b?.name),
            sortOrder: sortedInfo.columnKey === "name" ? sortedInfo.order : null,
            render: (name) => <Tag color="success">{name}</Tag>,
        },
        {
            title: "IP-адрес",
            key: "ipAddress",
            dataIndex: "ipAddress",
            width: "5%",
            ...getColumnSearchProps("ipAddress", "IP-адрес"),
            sorter: (a, b) => a?.ipAddress?.localeCompare(b?.ipAddress),
            sortOrder: sortedInfo.columnKey === "ipAddress" ? sortedInfo.order : null,
        },
    ];

    if (userRole === managerMenuItems.users.admin.key) {
        monitoringColumns = monitoringColumns.concat({
            title: "Управление",
            dataIndex: "idd",
            key: "idd",
            width: "1%",
            render: (idd, record) => (
                <>
                    <Button
                        style={{
                            width: "100%",
                            backgroundColor: blueColor,
                            color: "white",
                        }}
                        onClick={() => {
                            setSelectedId(idd);
                            setSelectedIp(record.ipAddress);
                            setModalOpen(true);
                            form.setFieldsValue({ipAddress: record.ipAddress});
                        }}
                    >
                        Настроить
                    </Button>
                </>
            ),
        });
    }
    return (
        <>
            <Table
                locale={monitoringTableLocale}
                columns={monitoringColumns}
                dataSource={rooms}
                onChange={handleChange}
            />
            <Modal
                title="Управление операционным блоком"
                centered
                open={modalOpen}
                onCancel={() => setModalOpen(false)}
                footer={null}
                afterClose={() => {
                    setSelectedId(null);
                    setSelectedIp("");
                }}
            >
                <Form
                    form={form}
                    name="basic"
                    initialValues={{
                        ipAddress: selectedIp,
                    }}
                    labelCol={{
                        span: 5,
                    }}
                    wrapperCol={{
                        span: 20,
                    }}
                    onFinish={(values) => {
                        if (values.ipAddress !== selectedId) {
                            updateOperatingRoomIp(selectedId, values.ipAddress);
                        } else {
                            message.info(
                                <span>{`IP-адрес операционной имеет установленное значение. Обновление не требуется.`}</span>,
                            );
                        }
                        setModalOpen(false);
                    }}
                    autoComplete="off"
                >
                    <Form.Item
                        label="IP-адрес"
                        name="ipAddress"
                        rules={[
                            {
                                pattern: ipPattern,
                                message: "Неверное значение IP-адреса!",
                            },
                        ]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        wrapperCol={{
                            offset: 5,
                            span: 20,
                        }}
                    >
                        <Button type="primary" htmlType="submit" style={{width: "100%"}}>
                            Сохранить IP-адрес
                        </Button>
                    </Form.Item>
                    <Form.Item
                        wrapperCol={{
                            offset: 5,
                            span: 20,
                        }}
                    >
                        <Button
                            type="primary"
                            danger
                            style={{width: "100%"}}
                            onClick={() => deleteOperatingRoom(selectedId)}
                        >
                            Удалить операционный блок
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
};

export default OperatingRoomTable;
