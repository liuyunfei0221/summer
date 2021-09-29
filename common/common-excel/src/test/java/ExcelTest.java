//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.function.BiConsumer;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class ExcelTest {
//
//    /**
//     *
//     * @param exportByIdsDTO
//     * @return
//     */
//    @PostMapping(value = "/recommender/list/exportExcel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    @OperationLog(menu = "文件导出", method = "导出", desc = "导出推广员列表Excel")
//    public void exportRecommenderInfoExcel(@RequestHeader("X-Token") String token, @RequestBody @Validated ExportByIdsDTO exportByIdsDTO,
//                                           HttpServletRequest request, HttpServletResponse response) {
//
//        LOGGER.info("exportByIdsDTO = {}", exportByIdsDTO.toString());
//
//        Long[] ids = exportByIdsDTO.getIds();
//
//        int len = Optional.of(ids)
//                .map(idArr -> idArr.length).orElse(0);
//
//        if (len < EXPORT_MIN || len > EXPORT_MAX) {
//            throw new BusinessException(AuthBusinessError.SYS_REQUEST_PARAMETER_ERROR,
//                    "导出文件条数不能小于" + EXPORT_MIN + "条或多于" + EXPORT_MAX + "条");
//        }
//
//        List<RecommenderDO> recommenders = recommenderService.selectRecommenderByIds(
//                Stream.of(ids).distinct().collect(Collectors.toList()))
//                .stream()
//                .sorted((a, b) -> (b.getGmtCreate().intValue() - a.getGmtCreate().intValue()))
//                .collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(recommenders)) {
//            throw new BusinessException(AuthBusinessError.SYS_REQUEST_PARAMETER_ERROR,
//                    "无导出数据");
//        }
//
//        List<CityDTO> cityList = cityService.getCityList();
//        Map<Integer, String> codeNameMapping = new HashMap<>(cityList.size());
//        cityList.forEach(c -> codeNameMapping.put(c.getCode(), c.getName()));
//
//        AtomicInteger atomicInteger = new AtomicInteger();
//        BiConsumer<XSSFRow, RecommenderDO> rowElementPackeger = (r, e) -> {
//            XSSFCell cell = r.createCell(0);
//            cell.setCellType(CellType.NUMERIC);
//            cell.setCellValue(atomicInteger.incrementAndGet());
//
//            cell = r.createCell(1);
//            cell.setCellType(CellType.STRING);
//            cell.setCellValue(e.getName());
//
//            cell = r.createCell(2);
//            cell.setCellType(CellType.NUMERIC);
//            cell.setCellValue(e.getId());
//
//            cell = r.createCell(3);
//            cell.setCellType(CellType.STRING);
//            cell.setCellValue(Optional.ofNullable(codeNameMapping.get(e.getCityCode())).orElse(""));
//
//            cell = r.createCell(4);
//            cell.setCellType(CellType.STRING);
//            cell.setCellValue(e.getPhone());
//
//            cell = r.createCell(5);
//            cell.setCellType(CellType.NUMERIC);
//            cell.setCellValue(e.getOrderCount());
//
//            cell = r.createCell(6);
//            cell.setCellType(CellType.STRING);
//            cell.setCellValue(FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(e.getGmtCreate()), ZoneId.systemDefault())));
//        };
//
//        WorkBookElement<RecommenderDO> workBookElement = new WorkBookElement<>(RECOMMENDER_HEADERS, rowElementPackeger);
//        WorkBookGenerator<RecommenderDO> generator = new WorkBookGenerator<>(workBookElement);
//        List<List<RecommenderDO>> pageElements = new ArrayList<>(Collections.singletonList(recommenders));
//        XSSFWorkbook xssfWorkbook = generator.generate(pageElements);
//
//        try (final ServletOutputStream outputStream = response.getOutputStream();
//             final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
//
//            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//
//            String fileName = "推荐人列表_" + FILE_NAME_FORMATTER.format(LocalDateTime.now()) + ".xlsx";
//
//            String disValue = "attachment; filename=\"" + processFileName(request, fileName) + "\"";
//            response.addHeader("Content-Disposition", disValue);
//
//            xssfWorkbook.write(bufferedOutputStream);
//        } catch (IOException e) {
//            LOGGER.error("导出推荐人列表异常,e = " + e);
//            throw new BusinessException(AuthBusinessError.SYS_INTERNAL_SERVER_ERROR, "导出文件失败");
//        }
//    }
//
//}
