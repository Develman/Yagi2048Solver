package de.fhaachen.ti.yagi2048solver;

import de.fh_aachen.fb5.ti.yagi.Yagi;
import de.fhaachen.ti.yagi2048solver.model.Grid;
import java.io.*;
import java.net.URL;
import java.util.Random;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("ws")
public class YagiSolverWebservice
{
    private static final Logger LOG = LoggerFactory.getLogger(YagiSolverWebservice.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response calculateNextStep(Grid grid) throws Exception
    {
        LOG.debug("Received new grid: " + grid);

        try
        {
            Yagi yagi = new Yagi();

            URL yagiFile = this.getClass().getClassLoader().getResource("2048.y");
            System.out.println("Using file: " + yagiFile);
            yagi.importFile(yagiFile.getFile());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream stream = new PrintStream(out);
            System.setOut(stream);

            yagi.prompt("check(1, 1, 1, 1)");
            String nextStep = getNextStep(out);

            return Response.status(Response.Status.OK).entity(nextStep).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getStackTrace(ex)).type(
                    MediaType.TEXT_PLAIN).build();
        }
    }

    private String getNextStep(ByteArrayOutputStream out) throws IOException
    {
        String nextStep = new String(out.toByteArray(), "UTF-8");
        nextStep = nextStep.replaceAll("\\r\\n|\\r|\\n", " ").trim();

        if (nextStep.equals("Random"))
        {
            nextStep = createRandom();
        }

        return "{\"value\": \"" + nextStep + "\"}";
    }

    private String createRandom()
    {
        Random rand = new Random();
        int nextRand = rand.nextInt(4);

        return Integer.toString(nextRand);
    }

    private static String getStackTrace(Throwable aThrowable)
    {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);

        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }
}
