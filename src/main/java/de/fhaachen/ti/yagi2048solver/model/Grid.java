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

        for (List<Integer> col : columns)
        {
            buf.append("[");
            for (Integer val : col)
            {
                buf.append(col);
                buf.append(",");
            }
            buf.append("],");
        }

        return buf.toString();
    }
}
