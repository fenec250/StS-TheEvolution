package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.MasteryPower;

import java.util.List;
import java.util.Optional;

public class Mastery2
		extends BaseEvoCard implements CustomSavable<String> {
	public static final String ID = "evolutionmod:Mastery";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
//	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalDust.png";
	private static final int COST = 1;
	private static final int FORM_TRIGGER = 1;

	private AbstractGene gene;

	public Mastery2() {
		this(null);
	}

	private Mastery2(AbstractGene gene) {
		super(ID, NAME, new RegionName("purple/power/mental_fortress"), COST, DESCRIPTION,
				CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = FORM_TRIGGER;
		this.exhaust = true;
		this.gene = gene;
		resetGene();
	}

	@Override
	public boolean canPlay(AbstractCard card) {
		if (card == this && gene == null
				&& AbstractDungeon.player.orbs.stream()
						.noneMatch(o -> o instanceof AbstractGene)) {
			this.cantUseMessage = EXTENDED_DESCRIPTION[6];
			return false;
		}
		return super.canPlay(card);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (gene == null) {
			Optional<AbstractOrb> nextGene = AbstractDungeon.player.orbs.stream()
					.filter(o -> o instanceof AbstractGene)
					.findFirst();
			if (nextGene.isPresent()) {
				gene = ((AbstractGene) nextGene.get());
				p.masterDeck.group.stream()
						.filter(c -> c.uuid.equals(this.uuid)).findAny()
						.ifPresent(c -> {
							((Mastery2) c).gene = this.gene;
							((Mastery2) c).resetGene();
						});
				resetGene();
			}
			else {
				return;
			}
		}
		addToBot(new ApplyPowerAction(p, p, new MasteryPower(p, this.magicNumber, gene.ID)));
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				if (upgraded) {
					addToTop(new ChannelAction(gene.makeCopy()));
					addToTop(new IncreaseMaxOrbAction(1));
				} else {
					formEffect(gene.ID, () -> addToTop(new IncreaseMaxOrbAction(1)));
				}
				this.isDone = true;
			}
		});
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public AbstractCard makeCopy() {
		return this.gene == null ? new Mastery2() : new Mastery2(this.gene);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
			this.resetGene();
//			this.upgradeBaseCost(UPGRADED_COST);
		}
	}

	@Override
	public void triggerOnGlowCheck() {
		String geneId = "";
		if (gene != null && !upgraded && isPlayerInThisForm(gene.ID)) {
			geneId = gene.ID;
		} else if (gene == null) {
			Optional<AbstractOrb> nextGene = AbstractDungeon.player.orbs.stream()
					.filter(o -> o instanceof AbstractGene)
					.findFirst();
			if(nextGene.isPresent()) {
				geneId = nextGene.get().ID;
			}
		}
		switch (geneId) {
			case HarpyGene.ID: this.glowColor = HarpyGene.COLOR.cpy(); return;
			case MerfolkGene.ID: this.glowColor = MerfolkGene.COLOR.cpy(); return;
			case LavafolkGene.ID: this.glowColor = LavafolkGene.COLOR.cpy(); return;
			case CentaurGene.ID: this.glowColor = CentaurGene.COLOR.cpy(); return;
			case LizardGene.ID: this.glowColor = LizardGene.COLOR.cpy(); return;
			case BeastGene.ID: this.glowColor = BeastGene.COLOR.cpy(); return;
			case PlantGene.ID: this.glowColor = PlantGene.COLOR.cpy(); return;
			case ShadowGene.ID: this.glowColor = ShadowGene.COLOR.cpy(); return;
			case LymeanGene.ID: this.glowColor = LymeanGene.COLOR.cpy(); return;
			case InsectGene.ID: this.glowColor = InsectGene.COLOR.cpy(); return;
			case SuccubusGene.ID: this.glowColor = SuccubusGene.COLOR.cpy(); return;
			default: this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy(); return;
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			super.getCustomTooltips();
			customTooltips.add(new TooltipInfo("Imprinted Form",
					"The #yForm on this card is selected the first time it is played, using the oldest channeled Gene."));
		}
		return customTooltips;
	}

	private void resetGene() {
		if (gene != null) {
			this.rawDescription = EXTENDED_DESCRIPTION[0]
					+ gene.ID + EXTENDED_DESCRIPTION[1]
					+ (!this.upgraded
					? gene.ID + EXTENDED_DESCRIPTION[2]
					: EXTENDED_DESCRIPTION[3] + gene.ID + EXTENDED_DESCRIPTION[4]);
			this.name = replaceGeneIds(this.gene.ID + EXTENDED_DESCRIPTION[5]);
		} else {
			this.name = NAME;
			this.rawDescription = this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;
		}
		initializeDescription();
		initializeTitle();
	}

	@Override
	public String onSave() {
		return this.gene != null ? gene.ID : "";
	}

	@Override
	public void onLoad(String geneId) {
		this.gene = getGene(geneId);
		resetGene();
	}
}