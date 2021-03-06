package de.fhaachen.ti.yagi2048solver.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Grid
{
    @XmlElement(name = "columns")
    private List<List<Integer>> columns;

    public void setColumns(List<List<Integer>> columns)
    {
        this.columns = columns;
    }

    public List<List<Integer>> getColumns()
    {
        return columns;
    }

    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append(" { columns: [");

        for (List<Integer> column : columns)
        {
            buf.append(column);
            buf.append(",");
        }

        buf.append("]");
        return buf.toString();
    }

    public boolean equals(Grid grid)
    {
        if (grid == null)
        {
            return false;
        }

        return columns.equals(grid.getColumns());
    }
}
