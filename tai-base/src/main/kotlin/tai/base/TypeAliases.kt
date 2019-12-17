package tai.base

typealias PrimitiveValue = Any
typealias JsonMap = Map<String, PrimitiveValue?>;
typealias JsonList = List<PrimitiveValue?>;
typealias JsonMapList = List<JsonMap>;

typealias MutableJsonMap = MutableMap<String, PrimitiveValue?>;
typealias MutableJsonList = MutableList<PrimitiveValue?>;
typealias MutableJsonMapList = MutableList<JsonMap>;

typealias JsonCollection = Collection<PrimitiveValue?>;
typealias JsonMapCollection = Collection<JsonMap>;

typealias MutableJsonCollection = MutableCollection<PrimitiveValue?>;
typealias MutableJsonMapCollection = MutableCollection<JsonMap>;