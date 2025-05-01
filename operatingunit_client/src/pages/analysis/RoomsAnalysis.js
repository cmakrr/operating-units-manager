import React, {useState} from "react";
import dayjs from "dayjs";
import AnalysisInfo from "./AnalysisInfo";
import {Button, Form, Input} from "antd";
import {managerMenuItems} from "../../const/constants";
import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import AnalysisDatePicker from "./AnalysisDatePicker";
import {getRoomsAnalysis} from "../../request/AnalysisRequests";

function RoomsAnalysis() {
    const [startDate, setStartDate] = useState(dayjs().add(-1, "d"));
    const [endDate, setEndDate] = useState(dayjs());
    const [analysisInfo, setAnalysisInfo] = useState();
    const [isInfoFetched, setIsInfoFetched] = useState(false);

    async function submitForm(values) {
        const dateRange = {
            "startDate": startDate,
            "endDate": endDate
        };

        const result = await getRoomsAnalysis(values.operationRoom, dateRange);

        if (result) {
            setAnalysisInfo(result);
            setIsInfoFetched(true);
        }
    }


    return (
        <MainHeader>
            <div style={{overflowX: "auto", width: "100%"}}>
                <ManagerSider
                    breadcrumb={
                        [managerMenuItems.plan.main.label, managerMenuItems.plan.create.label]
                    }
                    defaultKey={managerMenuItems.plan.create.key}
                >
                    <div>
                        {isInfoFetched ?
                            (<>
                                <AnalysisInfo info={analysisInfo}/>
                                <Button onClick={() => setIsInfoFetched(false)}>Назад</Button>
                            </>)
                            : (<>
                                <Form
                                    name="basic"
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 16}}
                                    onFinish={(values) => submitForm(values)}
                                    autoComplete="off"
                                    style={{maxWidth: '400px', margin: '0 auto'}}
                                >
                                    <AnalysisDatePicker startDate={startDate} endDate={endDate}
                                                        setStartDate={setStartDate} setEndDate={setEndDate}/>
                                    <Form.Item
                                        label="Операционная"
                                        name="operationRoom"
                                        rules={[
                                            {
                                                required: true,
                                                message:
                                                    "Пожалуйста, введите название операционной комнаты!",
                                            },
                                        ]}
                                    >
                                        <Input/>
                                    </Form.Item>
                                </Form>
                            </>)}
                    </div>
                </ManagerSider>
            </div>
        </MainHeader>
    );
}

export default RoomsAnalysis;
