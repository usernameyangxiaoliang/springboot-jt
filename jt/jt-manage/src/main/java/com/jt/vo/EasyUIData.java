package com.jt.vo;
import java.io.Serializable;
import java.util.List;
import com.jt.pojo.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUIData implements Serializable{
	private static final long serialVersionUID = 7234202634596333732L;
	private Integer total;
	private List<Item> rows;
}
