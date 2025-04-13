import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import React, {useEffect, useState} from "react";
import {managerMenuItems} from "../../const/constants";
import {getPatients} from "../../request/PatientRequests";

function PatientsPage() {
    const [patients, setPatients] = useState([]);

    useEffect(() => {
        async function fetchData() {
            try {
                const patientsData = await getPatients();
                setPatients(patientsData);
            } catch (error) {
                console.error("Произошла ошибка при получении информации об пациентах:", error);
            }
        }

        fetchData();
    }, []);

    return (
        <MainHeader>
            <div style={{overflowX: "auto", width: "100%"}}>
                <ManagerSider
                    breadcrumb={[managerMenuItems.patients.label]}
                    defaultKey={managerMenuItems.patients.key}
                >
                    <p/>
                    <div>
                        <PatientsPage patients={patients}/>
                    </div>
                    <p/>

                </ManagerSider>
            </div>
        </MainHeader>
    );
}

export default PatientsPage;
