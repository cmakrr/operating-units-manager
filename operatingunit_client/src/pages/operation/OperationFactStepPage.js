import React, { useEffect, useState } from "react";
import { useParams } from "react-router";
import { getOperationFact } from "../../request/Requests";
import MainHeader from "../../components/common/MainHeader";
import { StepDescriptions } from "../../components/model/operation/OperationDetailsDescriptions";
import {
  checkoutStepInfo,
  stepInfo,
} from "../../components/model/operation/OperationDetails";

function OperationFactStepPage() {
  const [operationFact, setOperationFact] = useState([]);
  const { id } = useParams();
  const { factId } = useParams();

  useEffect(() => {
    async function fetchData() {
      try {
        const stepData = await getOperationFact(id, factId);
        setOperationFact(stepData);
      } catch (error) {
        console.error("Произошла ошибка при получении операции:", error);
      }
    }
    fetchData();
  }, []);

  return (
    <MainHeader>
      {StepDescriptions(
        operationFact?.startTime
          ? stepInfo(id, operationFact)
          : checkoutStepInfo(id, operationFact),
      )}
    </MainHeader>
  );
}

export default OperationFactStepPage;
