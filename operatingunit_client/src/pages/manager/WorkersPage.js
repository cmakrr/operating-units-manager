import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import React, {useEffect, useState} from "react";
import {ipPattern, managerMenuItems} from "../../const/constants";
import {getPatients} from "../../request/PatientRequests";
import WorkersTable from "../../components/table/WorkersTable";
import {Button, Form, Input, message, Modal, Select} from "antd";
import {createOperatingRoom} from "../../request/OperatingRoomRequests";
import {saveWorker, updateWorker} from "../../request/WorkerRequests";
import {WorkerStatus} from "../../functions/MedicalWorker";

async function fetchData(setWorkers) {
    try {
        const workersData = await getPatients();
        setWorkers(workersData);
    } catch (error) {
        console.error("Произошла ошибка при получении информации об пациентах:", error);
    }
}

function WorkersPage() {
    const [workers, setWorkers] = useState([]);
    const [modalSave, setModalSave] = useState(false);
    const [modalUpdate, setModalUpdate] = useState(false);
    const [workerToUpdate, setWorkerToUpdate] = workerToUpdate;

    async function openUpdateWorker(id){
        setWorkerToUpdate(workers.find(x=>x.id === id));
        setModalUpdate(true);
    }

    async function saveNewWorker(worker){
        await saveWorker(worker);
        let newWorkersArr = [...workers, worker];
        setWorkers(newWorkersArr);
        message.success(<span>{`Работник успешно добавлен!`}</span>);
    }

    async function updateWorkerFields(worker){
        await updateWorker(worker);
        let newWorkersArr = workers.map(x => x.id === workerToUpdate.id ? worker : x);
        setWorkers(newWorkersArr);
        message.success(<span>{`Работник успешно обновлен!`}</span>);
    }

    async function removeWorker(){
        workerToUpdate.workerStatus = 2;
        await updateWorker(workerToUpdate);
        let newWorkersArr = workers.filter(x => x.id !== workerToUpdate.id);
        setWorkers(newWorkersArr);
        message.success(<span>{`Работник успешно удален!`}</span>);
    }

    useEffect(async () => {
        await fetchData();
    }, []);

    return (
        <MainHeader>
            <div style={{overflowX: "auto", width: "100%"}}>
                <ManagerSider
                    breadcrumb={[managerMenuItems.workers.label]}
                    defaultKey={managerMenuItems.workers.key}
                >
                    <p/>
                    <div>
                        <WorkersTable patientEntities={workers} openModal={openUpdateWorker}/>
                    </div>
                    <p/>
                    <Button
                        type="primary"
                        style={{ width: "100%" }}
                        onClick={() => setModalSave(true)}
                    >
                        Добавить работника
                    </Button>
                    <Modal
                        title="Добавление работника"
                        centered
                        open={modalSave}
                        initialValues={{
                            workerStatus: 0
                        }}
                        onCancel={() => setModal2Open(false)}
                        footer={null}
                    >
                        <Form
                            name="basic"
                            labelCol={{
                                span: 5,
                            }}
                            wrapperCol={{
                                span: 20,
                            }}
                            onFinish={(values) => saveNewWorker(values)}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="ФИО"
                                name="fullName"
                                rules={[
                                    {
                                        required: true,
                                        message:
                                            "Пожалуйста, введите ФИО работника!",
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>
                            <Form.Item
                                label="Должность"
                                name="position"
                                rules={[
                                    {
                                        required: true,
                                        message: "Пожалуйста, введите должность работника!",
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>

                            <Form.Item
                                label="Должность"
                                name="position"
                                rules={[
                                    {
                                        required: true,
                                        message: "Пожалуйста, введите должность работника!",
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>

                            <Form.Item name="workerStatus" label="Статус работника">
                                <Select>
                                    {Object.entries(WorkerStatus).map(([key, value]) => (
                                        <Select.Option key={key} value={Number(key)}>
                                            {value}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>

                            <Form.Item
                                wrapperCol={{
                                    offset: 5,
                                    span: 20,
                                }}
                            >
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    style={{ width: "100%" }}
                                >
                                    Создать
                                </Button>
                            </Form.Item>
                        </Form>
                    </Modal>
                    <Modal
                        title="Добавление работника"
                        centered
                        open={modalUpdate}
                        initialValues={{
                            ...workerToUpdate
                        }}
                        onCancel={() => setModalUpdate(false)}
                        footer={null}
                    >
                        <Form
                            name="basicUpdate"
                            labelCol={{
                                span: 5,
                            }}
                            wrapperCol={{
                                span: 20,
                            }}
                            onFinish={(values) => updateWorkerFields(values)}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="ФИО"
                                name="fullName"
                                rules={[
                                    {
                                        required: true,
                                        message:
                                            "Пожалуйста, введите ФИО работника!",
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>
                            <Form.Item
                                label="Должность"
                                name="position"
                                rules={[
                                    {
                                        required: true,
                                        message: "Пожалуйста, введите должность работника!",
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>

                            <Form.Item
                                label="Должность"
                                name="position"
                                rules={[
                                    {
                                        required: true,
                                        message: "Пожалуйста, введите должность работника!",
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>

                            <Form.Item name="workerStatus" label="Статус работника">
                                <Select>
                                    {Object.entries(WorkerStatus).map(([key, value]) => (
                                        <Select.Option key={key} value={Number(key)}>
                                            {value}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>

                            <Form.Item
                                wrapperCol={{
                                    offset: 5,
                                    span: 20,
                                }}
                            >
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    style={{ width: "100%" }}
                                >
                                    Обновить
                                </Button>
                                <Button
                                    type="primary"
                                    onClick={() => removeWorker()}
                                    danger
                                    style={{ width: "100%" }}
                                >
                                    Удалить
                                </Button>
                            </Form.Item>
                        </Form>
                    </Modal>
                </ManagerSider>
            </div>
        </MainHeader>
    );
}

export default WorkersPage;
