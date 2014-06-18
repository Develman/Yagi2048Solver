package de.fhaachen.ti.yagi2048solver;

import de.fh_aachen.fb5.ti.yagi.Yagi;
import de.fhaachen.ti.yagi2048solver.model.Grid;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ws")
public class YagiSolverWebservice
{
    private final static Logger LOG = Logger.getLogger(YagiSolverWebservice.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response calculateNextDirection(Grid grid) throws Exception
    {
        LOG.log(Level.INFO, "Received new grid: {0}", grid);

        try
        {
            Yagi yagi = new Yagi();
            importYagiFile(yagi, "2048_better.y");

            ByteArrayOutputStream out = redirectOutput();

            String rowPrompt = buildRowPrompt(grid);
            LOG.log(Level.INFO, "Sending rowPrompt: {0}", rowPrompt);
            yagi.prompt(rowPrompt); // "procInitRow(2,2,0,0, 4,2,0,0, 8,8,0,0, 16,2,2,0)");
            yagi.prompt("procEvaluateDirection()");

            String[] result1 = getResult(out);
            LOG.log(Level.INFO, "Result 1: [direction={0},maxVal={1}]", new Object[]
            {
                result1[1], result1[0]
            });
            out.reset();

            String columnPrompt = buildColumnPrompt(grid);
            LOG.log(Level.INFO, "Sending columnPrompt: {0}", columnPrompt);
            yagi.prompt(columnPrompt); // "procInitRow(2,2,0,0, 4,2,0,0, 8,8,0,0, 16,2,2,0)");
            yagi.prompt("procEvaluateDirection()");

            String[] result2 = getResult(out);
            LOG.log(Level.INFO, "Result 2: [direction={0},maxVal={1}]", new Object[]
            {
                result2[1], result2[0]
            });
            out.reset();

            String direction = evaluateDirection(result1, result2);
            LOG.log(Level.INFO, "Sending direction: {0}", direction);
            return Response.status(Response.Status.OK).entity(direction).build();
        }
        catch (Exception ex)
        {
            LOG.log(Level.WARNING, "Yagi error", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getStackTrace(ex)).type(
                    MediaType.TEXT_PLAIN).build();
        }
    }

    public Response oldCalculateNextStep(Grid grid) throws Exception
    {
        LOG.log(Level.INFO, "Received new grid: {0}", grid);

        try
        {
            Yagi yagi = new Yagi();

            URL yagiFile = this.getClass().getClassLoader().getResource("2048.y");
            yagi.importFile(yagiFile.getFile());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream stream = new PrintStream(out);
            System.setOut(stream);

            yagi.prompt("check(1, 1, 1, 1)");
            String nextStep = ""; //getNextStep(out);

            return Response.status(Response.Status.OK).entity(nextStep).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getStackTrace(ex)).type(
                    MediaType.TEXT_PLAIN).build();
        }
    }

    private void importYagiFile(Yagi yagi, String filename) throws IOException
    {
        URL yagiFile = this.getClass().getClassLoader().getResource(filename);
        LOG.log(Level.INFO, "Using file: {0}", yagiFile);
        yagi.importFile(yagiFile.getFile());
    }

    private ByteArrayOutputStream redirectOutput()
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(out);
        System.setOut(stream);

        return out;
    }

    private String buildRowPrompt(Grid grid)
    {
        StringBuilder rowPrompt = new StringBuilder();
        rowPrompt.append("procInitRow(");

        int columnSize = grid.getColumns().size();
        for (int i = 0; i < columnSize; i++)
        {
            for (int j = 0; j < columnSize; j++)
            {
                rowPrompt.append(grid.getColumns().get(j).get(i));
                if (i != columnSize - 1 || j != columnSize - 1)
                {
                    rowPrompt.append(",");
                }
            }
        }

        rowPrompt.append(")");
        return rowPrompt.toString();
    }

    private String buildColumnPrompt(Grid grid)
    {
        StringBuilder columnPrompt = new StringBuilder();
        columnPrompt.append("procInitRow(");

        int columnSize = grid.getColumns().size();
        for (int i = 0; i < columnSize; i++)
        {
            List<Integer> column = grid.getColumns().get(i);
            for (int j = 0; j < columnSize; j++)
            {
                columnPrompt.append(column.get(j));
                if (i != columnSize - 1 || j != columnSize - 1)
                {
                    columnPrompt.append(",");
                }
            }
        }

        columnPrompt.append(")");
        return columnPrompt.toString();
    }

    private String[] getResult(ByteArrayOutputStream out) throws IOException
    {
        String result = new String(out.toByteArray(), "UTF-8");
        return result.split(System.lineSeparator());
    }

    private String evaluateDirection(String[] result1, String[] result2)
    {
        if (Integer.parseInt(result1[1]) > Integer.parseInt(result2[1]))
        {
            int a = Integer.parseInt(result1[0]);
            if (a == 0)
            {
                return "{\"value\": \"" + 3 + "\"}";
            }
            else
            {
                return "{\"value\": \"" + 1 + "\"}";
            }
        }
        else
        {
            int a = Integer.parseInt(result2[0]);
            if (a == 0)
            {
                return "{\"value\": \"" + 0 + "\"}";
            }
            else
            {
                return "{\"value\": \"" + 2 + "\"}";
            }
        }
    }

    private static String getStackTrace(Throwable aThrowable)
    {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);

        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }
}
