package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mutate extends BaseEvoCard {
	public static final String ID = "evolutionmodV2:Mutate";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/Mutate.png";
	private static final int COST = 0;
	private static final int GENE_CHOICES = 3;

	public Mutate() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = GENE_CHOICES;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		this.addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				List<String> current = p.orbs.stream()
						.filter(o -> o != null && o.ID != null)
						.map(o -> o.ID)
						.collect(Collectors.toList());
				List<String> available = new ArrayList<>();
				if (!current.contains(PlantGene2.ID)) { available.add(PlantGene2.ID);}
				if (!current.contains(MerfolkGene2.ID)) { available.add(MerfolkGene2.ID);}
				if (!current.contains(HarpyGene2.ID)) { available.add(HarpyGene2.ID);}
				if (!current.contains(LavafolkGene2.ID)) { available.add(LavafolkGene2.ID);}
				if (!current.contains(SuccubusGene2.ID)) { available.add(SuccubusGene2.ID);}
				if (!current.contains(LymeanGene2.ID)) { available.add(LymeanGene2.ID);}
				if (!current.contains(InsectGene2.ID)) { available.add(InsectGene2.ID);}
				if (!current.contains(BeastGene2.ID)) { available.add(BeastGene2.ID);}
				if (!current.contains(LizardGene2.ID)) { available.add(LizardGene2.ID);}
				if (!current.contains(CentaurGene2.ID)) { available.add(CentaurGene2.ID);}
				if (!current.contains(ShadowGene2.ID)) { available.add(ShadowGene2.ID);}

				if (available.size() == 1) {
					addToTop(getGene(available.get(0)).getChannelAction());
				} else {
					while (available.size() > magicNumber) {
						available.remove(AbstractDungeon.cardRandomRng.random(available.size() - 1));
					}
					ArrayList<AbstractCard> cardChoices = new ArrayList<>(available.stream()
							.map(o -> {
								Gene gene = new Gene(getGene(o));
								if (upgraded) {gene.upgrade();}
								return gene;
							})
							.collect(Collectors.toList()));
					addToTop(new ChooseOneAction(cardChoices));
				}
				this.isDone = true;
			}
		});
	}

	@Override
	public AbstractCard makeCopy() {
		return new Mutate();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.exhaust = false;
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}