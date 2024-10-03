import React, { useEffect, useState } from "react";
import { useParams } from "react-router";
import MainHeader from "../../components/common/MainHeader";
import { getOperationById } from "../../request/Requests";
import { OperationDetailsDescriptions } from "../../components/model/operation/OperationDetailsDescriptions";
import { operationCheckoutInfo } from "../../components/model/operation/OperationDetails";

function OperationFactStartCheckoutPage() {
  const [operation, setOperation] = useState([]);
  const { id } = useParams();

  useEffect(() => {
    async function fetchData() {
      try {
        const operationsData = await getOperationById(id);
        setOperation(operationsData);
      } catch (error) {
        console.error("Произошла ошибка при получении операции:", error);
      }
    }
    fetchData();
  }, []);

  return (
    <MainHeader>
      {OperationDetailsDescriptions(
        operation,
        operationCheckoutInfo(operation),
      )}
    </MainHeader>
  );
}

export default OperationFactStartCheckoutPage;
