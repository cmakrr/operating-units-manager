import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {clientApi} from "../../const/api/clientApi";
import {getOperatingRoomName} from "../../request/OperatingRoomRequests";


function CommonLoginPage() {
    const [roomName, setRoomName] = useState("");
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        async function fetchData() {
            const roomName = await getOperatingRoomName();
            setRoomName(roomName);
            setLoading(false);
        }

        fetchData();
    }, []);

    useEffect(() => {
        if (!loading) {
            if (roomName) {
                navigate(clientApi.login.operatingRoom + `?roomName=${roomName}`);
            } else {
                navigate(clientApi.login.user);
            }
        }
    }, [roomName, navigate, loading]);
}

export default CommonLoginPage;
