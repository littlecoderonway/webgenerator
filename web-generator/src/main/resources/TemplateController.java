@Controller
@RequestMapping("/{paramClassName}")
public class {className}Controller extends BaseController {

	@Autowired
	private {className}Service {paramClassName}Service;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return "{paramClassName}List";
	}

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Object save(final {className} {paramClassName}) {
		final String admin = ((Admin)(session.getAttribute(Constant.admin_session_key))).getAdminName();
		if(!check{className}({paramClassName}.getUserId())){
			return new Success();
		}
		return executeSuccess(new DefaultControllerCallback() {
			@Override
			public boolean doSuccessService() throws Exception {
				return {paramClassName}Service.saveOrUpdate({paramClassName},admin);
			}
		});

	}

	@ResponseBody
	@RequestMapping("/invalid")
	public Success delete(final Integer id) throws Exception {
		return executeSuccess(new DefaultControllerCallback() {
			@Override
			public boolean doSuccessService() throws Exception {
				return {paramClassName}Service.delete{className}(id);
			}
		});
	}
	
	@ResponseBody
	@RequestMapping(value = { "/data" }, method = RequestMethod.GET)
	public Object data(Integer id, {queryParamsWithType}
			final Integer page, final Integer rows) throws Exception {

		final Map<String, Object>  paramMap = buildParamMap(id,{queryParams});
		Grid<{className}> grid = executeGrid(new DefaultControllerCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public Grid<{className}> doPageService() throws Exception {
				Grid<{className}> grid = new Grid<{className}>();
				int totalNum = {paramClassName}Service.countByCriteri(paramMap);
				grid.countPage(totalNum, page, rows);
				countPageParam(grid,paramMap);
				grid.setRows({paramClassName}Service.listByCriteria(paramMap));
				return grid;
			}
		});
		return JSONObject.fromObject(grid, JsonTimeFormatConfig.getJsonConfig());
	}

	
	private Map<String, Object> buildParamMap(Integer id, {queryParamsWithType}) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (id!=null) {
			param.put("id", id);
		}
		{addQueryParam}
		return param;
	}
	

}
