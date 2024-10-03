import React, { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router";
import MainHeader from "../../components/common/MainHeader";
import { getOperationById } from "../../request/Requests";
import {
  OperationDetailsDescriptions,
  OperationDetailsWithFactEditDescriptions,
} from "../../components/model/operation/OperationDetailsDescriptions";
import {
  operationCheckoutInfo,
  operationInfo,
} from "../../components/model/operation/OperationDetails";

function OperationDetailsPage() {
  const [operation, setOperation] = useState([]);
  const { id } = useParams();

  useEffect(() => {
    async function fetchData() {
      try {
        const operationsData = await getOperationById(id, isManagement);
        setOperation(operationsData);
      } catch (error) {
        }
    }

    fetchData();
  }, []);

  const location = useLocation();
  const isEdit = location.pathname.endsWith("/edit");
  const isCheckout = location.pathname.includes("/checkout");
  const isManagement = location.pathname.includes("/management");

  return (
    <MainHeader>
      {isEdit
        ? OperationDetailsWithFactEditDescriptions(operation)
        : isCheckout
          ? OperationDetailsDescriptions(
              operation,
              operationCheckoutInfo(operation),
            )
          : OperationDetailsDescriptions(
              operation,
              operationInfo(operation, isManagement),
            )}
    </MainHeader>
  );
}

export default OperationDetailsPage;
