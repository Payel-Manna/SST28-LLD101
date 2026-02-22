public abstract class Exporter {
    // implied "contract" but not enforced (smell)

    public final ExportResult export(ExportRequest req) {

        if(req == null)
            throw new IllegalArgumentException("request cannot be null");

        ExportResult result = doExport(req);

        if(result == null ||
           result.contentType == null ||
           result.bytes == null)
        {
            throw new IllegalStateException("Invalid export result");
        }

        return result;
    }

    protected abstract ExportResult doExport(ExportRequest req);
}
