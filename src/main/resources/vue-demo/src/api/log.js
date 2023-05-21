import http from "@/utils/axios";

//日志分页
export const apiLogList = (param) => {
  return http({
    method: 'GET',
    url: '/log/list',
    data: param
  })
}

//日志分页
export const apiAuditLog = (param) => {
  return http({
    method: 'POST',
    url: '/log/audit',
    data: param
  })
}

//压缩归档
export const apiCompressArchive = (param) => {
  return http({
    method: 'POST',
    url: '/log/compress-archive',
    data: param
  });
}