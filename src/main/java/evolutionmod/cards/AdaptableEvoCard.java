package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public abstract class AdaptableEvoCard extends CustomCard {

//	private List<AbstractAdaptation> adaptations;
	protected Map<String, AbstractAdaptation> adaptationMap;

    public AdaptableEvoCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                            final CardType type, final CardColor color,
                            final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
//	    this.adaptations = new LinkedList<>();
	    this.adaptationMap = new HashMap<>();
    }

    public int addAdaptation(AbstractAdaptation adaptation) {
//    	adaptations.add(adaptation);
    	if (this.adaptationMap.containsKey(adaptation.getGeneId())) {
    		this.adaptationMap.get(adaptation.getGeneId()).amount += adaptation.amount;
    		if (this.adaptationMap.get(adaptation.getGeneId()).amount <= 0) {
			    this.adaptationMap.remove(adaptation.getGeneId());
		    }
	    } else {
		    if (adaptationMap.isEmpty()) { // TODO this can trigger multiple times if something removes adaptations
			    this.name += "&";
			    this.rawDescription += " NL ";
		    }
    		this.adaptationMap.put(adaptation.getGeneId(), adaptation);
		    this.rawDescription += adaptation.text() + " "; //TODO: add a method to get the description segment?
		    this.initializeDescription();
	    }
	    return adaptation.amount;
    }

	@Override
	public AbstractCard makeCopy() {
		AdaptableEvoCard card = (AdaptableEvoCard) super.makeCopy();
//		card.adaptations.addAll(this.adaptations);
		card.adaptationMap.putAll(this.adaptationMap);
		return card;
	}

	protected void useAdaptations(AbstractPlayer p, AbstractMonster m) {
//    	adaptations.forEach(f -> f.apply(p, m));
    	this.adaptationMap.values().forEach(f -> f.apply(p, m));
    }
//
//    public void selfAdapt(AbstractPlayer player) {
//    	getFilteredGenes(player, o -> true).stream()
//			    .map(AbstractGene::getAdaptation)
//			    .forEach(this::addAdaptation);
//    }
//
//    protected List<AbstractGene> getFilteredGenes(AbstractPlayer player, Predicate<AbstractGene> filter) {
//    	return player.orbs.stream()
//			    .filter(o -> o instanceof AbstractGene)
//			    .map(o -> (AbstractGene) o)
//			    .filter(filter)
//			    .collect(Collectors.toList());
//    }

    public abstract static class AbstractAdaptation {
    	public int amount;
    	public AbstractAdaptation(int amount) {
    		this.amount = amount;
	    }
	    public abstract void apply(AbstractPlayer player, AbstractMonster monster);
	    public abstract String text();
	    public abstract String getGeneId();
    }
//    public static class TempoNumber extends DynamicVariable {
//
//        @Override
//        public int baseValue(AbstractCard card) {
//            return ((AdaptableEvoCard) card).tempo;
//        }
//
//        @Override
//        public boolean isModified(AbstractCard card) {
//            return ((AdaptableEvoCard) card).tempoModified;
//        }
//
//        @Override
//        public String key() {
//            return "melodyMod:T";
//        }
//
//        @Override
//        public boolean upgraded(AbstractCard card) {
//            return ((AdaptableEvoCard) card).tempoUpgraded;
//        }
//
//        @Override
//        public int value(AbstractCard card) {
//            return ((AdaptableEvoCard) card).tempo;
//        }
//    }
}