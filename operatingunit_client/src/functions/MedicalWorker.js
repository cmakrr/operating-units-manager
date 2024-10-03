export function getMedicalWorkerInfo(worker) {
  const result =
    worker && (worker?.fullName || worker?.role)
      ? worker?.fullName || worker?.role
      : undefined;
  if (result) {
    return result + "\n";
  } else {
    return result;
  }
}

export function getMedicalWorkerFactInfo(planWorker, factWorker) {
  const worker = getMedicalWorkerInfo(factWorker);
  return worker ? worker : getMedicalWorkerInfo(planWorker);
}
