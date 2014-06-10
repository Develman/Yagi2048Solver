fluent result = {};

action checkUp($up, $down, $left, $right)
    precondition:
        (($up >= $down and $up >= $left) and $up >= $right)
    effect:
        result = {0};
    signal:
        "0" 
end action

action checkDown($up, $down, $left, $right)
    precondition:
        (($down >= $up and $down >= $left) and $down >= $right)
    effect:
        result = {1};
    signal:
        "2" 
end action

action checkLeft($up, $down, $left, $right)
    precondition:
        (($left >= $up and $left >= $down) and $left >= $right)
    effect:
        result = {2};
    signal:
        "3" 
end action

action checkRight($up, $down, $left, $right)
    precondition:
        (($right >= $up and $right >= $down) and $right >= $left)
    effect:
        result = {3};
    signal:
        "1" 
end action

action default($up, $down, $left, $right)
    precondition:
        (($right == $up and $right == $down) and $right == $left)
    effect:
        result = {8};
    signal:
        "Random" 
end action

action print($a)
	signal:
		"print " + $a
end action

proc printArray()
	for $n in result do
		print($n);
	end for
end proc

proc check($up, $down, $left, $right)
    choose
	checkUp($up, $down, $left, $right);
    or
	checkDown($up, $down, $left, $right);
    or
	checkLeft($up, $down, $left, $right);
    or
	checkRight($up, $down, $left, $right);
    or 
        default($up, $down, $left, $right);
    end choose
end proc