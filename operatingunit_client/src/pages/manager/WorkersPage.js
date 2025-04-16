import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import React, {useEffect, useState} from "react";
import {managerMenuItems} from "../../const/constants";
import WorkersTable from "../../components/table/WorkersTable";
import {Button, Form, Input, message, Modal, Select} from "antd";
import {getWorkers, saveWorker, updateWorker} from "../../request/WorkerRequests";
import {WorkerStatus} from "../../functions/MedicalWorker";

function WorkersPage() {
    const [workers, setWorkers] = useState([]);
    const [modalSave, setModalSave] = useState(false);
    const [modalUpdate, setModalUpdate] = useState(false);
    const [workerToUpdate, setWorkerToUpdate] = useState({});
    const [form] = Form.useForm();

    async function fetchData() {
        try {
            const workersData = await getWorkers();
            setWorkers(workersData);
        } catch (error) {
            console.error("Произошла ошибка при получении информации о работниках:", error);
        }
    }

    async function openUpdateWorker(id){
        let worker = workers.find(x=>x.id === id);
        setWorkerToUpdate(worker);
        setModalUpdate(true);
        form.setFieldsValue(worker);
    }

    async function saveNewWorker(worker){
        await saveWorker(worker);
        let newWorkersArr = [...workers, worker];
        setWorkers(newWorkersArr);
        await fetchData();
        message.success(<span>{`Работник успешно добавлен!`}</span>);
    }

    async function updateWorkerFields(values){
        let updatedWorker = {...workerToUpdate};
        updatedWorker.workerStatus = values.workerStatus;
        updatedWorker.fullName = values.fullName;
        updatedWorker.position = values.position;
        await updateWorker(updatedWorker);
        let newWorkersArr = workers.map(x => x.id === workerToUpdate.id ? updatedWorker : x);
        setWorkers(newWorkersArr);
        message.success(<span>{`Работник успешно обновлен!`}</span>);
    }

    async function removeWorker(){
        workerToUpdate.workerStatus = "DELETED";
        await updateWorker(workerToUpdate);
        let newWorkersArr = workers.filter(x => x.id !== workerToUpdate.id);
        setWorkers(newWorkersArr);
        setModalUpdate(false);
        message.success(<span>{`Работник успешно удален!`}</span>);
    }

    useEffect(() => {
        fetchData();
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
                        <WorkersTable workerEntities={workers} openModal={openUpdateWorker}/>
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
                        onCancel={() => setModalSave(false)}
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
                            initialValues={{ workerStatus: "WORKING" }}
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


                            <Form.Item name="workerStatus" label="Статус">
                                <Select>
                                    {Object.entries(WorkerStatus).map(([key, value]) => (
                                        <Select.Option key={key} value={key}>
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
                        title="Обновление работника"
                        centered
                        open={modalUpdate}
                        onCancel={() => setModalUpdate(false)}
                        footer={null}
                    >
                        <Form
                            name="basicUpdate"
                            form={form}
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
                            >
                                <Input />
                            </Form.Item>

                            <Form.Item name="workerStatus" label="Статус">
                                <Select>
                                    {Object.entries(WorkerStatus).map(([key, value]) => (
                                        <Select.Option key={key} value={key}>
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
                                    style={{ marginTop:"10px", width: "100%" }}
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
