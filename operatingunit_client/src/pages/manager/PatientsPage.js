import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import React, {useEffect, useState} from "react";
import {managerMenuItems} from "../../const/constants";
import {dischargePatient, getPatients, savePatient, updatePatient} from "../../request/PatientRequests";
import PatientsTable from "../../components/table/PatientsTable";
import {Button, DatePicker, Form, Input, InputNumber, message, Modal, Select} from "antd";
import {WorkerStatus} from "../../functions/MedicalWorker";
import {saveWorker, updateWorker} from "../../request/WorkerRequests";
import dayjs from "dayjs";

function PatientsPage() {
    const [patients, setPatients] = useState([]);
    const [modalSave, setModalSave] = useState(false);
    const [modalUpdate, setModalUpdate] = useState(false);
    const [patientToUpdate, setPatientToUpdate] = useState({});
    const [form] = Form.useForm();

    useEffect(() => {
        fetchData();
    }, []);

    async function fetchData() {
        try {
            const patientsData = await getPatients();
            setPatients(patientsData);
        } catch (error) {
            console.error("Произошла ошибка при получении информации об пациентах:", error);
        }
    }

    async function openUpdatePatient(id){
        let patient = patients.find(x=>x.id === id);
        if(patient.birthYear !== null){
            patient.birthDateToUpdate = dayjs(patient.birthYear);
        }
        setPatientToUpdate(patient);
        setModalUpdate(true);
        form.setFieldsValue(patient);
    }

    async function saveNewPatient(patient){
        patient.status = "IN_HOSPITAL";
        await savePatient(patient);
        await fetchData();
        message.success(<span>{`Работник успешно добавлен!`}</span>);
    }

    async function updatePatientFields(values){
        let updatedPatient = {...patientToUpdate};
        updatedPatient.fullName = values.fullName;
        updatedPatient.description = values.description;
        updatedPatient.roomNumber = values.roomNumber;
        updatedPatient.birthYear = values.birthDateToUpdate;
        await updatePatient(updatedPatient);
        updatedPatient.birthYear = values.birthDateToUpdate.format('YYYY-MM-DD');;
        let newPatientsArr = patients.map(x => x.id === patientToUpdate.id ? updatedPatient : x);
        setPatients(newPatientsArr);
        message.success(<span>{`Пациент успешно обновлен!`}</span>);
    }

    async function dischargePatientById(id) {
        await dischargePatient(id);

        message.success(<span>{`Пациент был выписан успешно!`}</span>);

        removePatient(id, patients, setPatients);
        setModalUpdate(false);
    }

    function removePatient(id, patients){
        const newPatients = patients.filter(x=>x.id !== id);
        setPatients(newPatients);
    }

    return (
        <MainHeader>
            <div style={{overflowX: "auto", width: "100%"}}>
                <ManagerSider
                    breadcrumb={[managerMenuItems.patients.label]}
                    defaultKey={managerMenuItems.patients.key}
                >
                    <p/>
                    <div>
                        <PatientsTable patientEntities={patients} openModal={openUpdatePatient}/>
                    </div>
                    <p/>
                    <Button
                        type="primary"
                        style={{ width: "100%" }}
                        onClick={() => setModalSave(true)}
                    >
                        Добавить пациента
                    </Button>
                    <Modal
                        title="Добавление пациента"
                        centered
                        open={modalSave}
                        onCancel={() => setModalSave(false)}
                        footer={null}
                    >
                        <Form
                            name="basic"
                            labelCol={{
                                span: 6,
                            }}
                            wrapperCol={{
                                span: 20,
                            }}
                            onFinish={(values) => saveNewPatient(values)}
                            autoComplete="off"
                        >
                            <Form.Item
                                labelCol={{ span: 6 }}
                                label="ФИО"
                                name="fullName"
                                rules={[
                                    {
                                        required: true,
                                        message:
                                            "Пожалуйста, введите ФИО пациента!",
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>
                            <Form.Item
                                labelCol={{ span: 6 }}
                                label="Палата"
                                name="roomNumber"
                                rules={[
                                    {
                                        required: true,
                                        message: "Пожалуйста, введите номер палаты!",
                                    },
                                ]}
                            >
                                <InputNumber min={1}   />
                            </Form.Item>

                            <Form.Item
                                labelCol={{ span: 6 }}
                                name="birthYear"
                                label="Дата рождения"
                                rules={[{ required: true, message: 'Пожалуйста, выберите дату рождения' }]}
                            >
                                <DatePicker
                                    format="YYYY-MM-DD"
                                    disabledDate={(current) => current && current > dayjs().endOf('day')}
                                />
                            </Form.Item>

                            <Form.Item
                                label="Описание"
                                name="description"
                                labelCol={{ span: 6 }}
                            >
                                <Input  />
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
                        title="Обновление пациента"
                        centered
                        open={modalUpdate}
                        onCancel={() => setModalUpdate(false)}
                        footer={null}
                    >
                        <Form
                            name="basicUpdate"
                            form={form}
                            labelCol={{
                                span: 6,
                            }}
                            wrapperCol={{
                                span: 20,
                            }}
                            onFinish={(values) => updatePatientFields(values)}
                            autoComplete="off"
                        >
                        <Form.Item
                            label="ФИО"
                            name="fullName"
                            rules={[
                                {
                                    required: true,
                                    message:
                                        "Пожалуйста, введите ФИО пациента!",
                                },
                            ]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            label="Палата"
                            name="roomNumber"
                            rules={[
                                {
                                    required: true,
                                    message: "Пожалуйста, введите номер палаты!",
                                },
                            ]}
                        >
                            <InputNumber min={1}   />
                        </Form.Item>

                        <Form.Item
                            name="birthDateToUpdate"
                            label="Дата рождения"
                            rules={[{ required: true, message: 'Пожалуйста, выберите дату рождения' }]}
                        >
                            <DatePicker
                                format="YYYY-MM-DD"
                                disabledDate={(current) => current && current > dayjs().endOf('day')}
                            />
                        </Form.Item>

                        <Form.Item
                            label="Описание"
                            name="description"
                        >
                            <Input  />
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
                                    onClick={() => dischargePatientById(patientToUpdate.id)}
                                    danger
                                    style={{ marginTop:"10px", width: "100%" }}
                                >
                                    Выписать
                                </Button>
                            </Form.Item>
                        </Form>
                    </Modal>
                </ManagerSider>
            </div>
        </MainHeader>
    );
}

export default PatientsPage;
