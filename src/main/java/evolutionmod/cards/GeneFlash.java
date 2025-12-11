package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.*;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.MasteryPower;

import java.util.List;
import java.util.Optional;

public class GeneFlash
		extends BaseEvoCard implements CustomSavable<String> {
	public static final String ID = "evolutionmod:Flash";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
//	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalDust.png";
	private static final int COST = 0;
	private static final int DAMAGE_AMT = 4;
	private static final int UPGRADE_DAMAGE_AMT = 3;

	private AbstractGene gene;

	public GeneFlash() {
		super(ID, NAME, new RegionName("purple/attack/bowling_bash"), COST, DESCRIPTION,
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.baseDamage = this.damage = DAMAGE_AMT;
		resetGene();
	}

	private GeneFlash(AbstractGene gene) {
		super(ID, NAME, new RegionName("purple/attack/bowling_bash"), COST, DESCRIPTION,
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.ENEMY);
		this.baseDamage = this.damage = DAMAGE_AMT;
		this.gene = gene;
		resetGene();
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
							((GeneFlash) c).gene = this.gene;
							((GeneFlash) c).resetGene();
						});
				resetGene();
			}
//			else {
//				return;
//			}
		}
		addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.FIRE));
		if (gene != null && p.orbs.stream().anyMatch(o -> gene.ID.equals(o.ID))) {
			addToBot(new EvokeSpecificOrbAction(
					p.orbs.stream().filter(o -> gene.ID.equals(o.ID)).findFirst().get()
			));
		} else {
			this.exhaustOnUseOnce = true;
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new GeneFlash(this.gene);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_DAMAGE_AMT);
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
			case ShadowGene2.ID: this.glowColor = ShadowGene2.COLOR.cpy(); return;
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
		if (!CardCrawlGame.isInARun()) {
			return;
		}
		if (gene != null) {
			this.name = replaceGeneIds(this.gene.ID + EXTENDED_DESCRIPTION[2]);
			this.rawDescription = gene == null ? DESCRIPTION : EXTENDED_DESCRIPTION[0]
					+ gene.ID + EXTENDED_DESCRIPTION[1];
		} else {
			this.name = NAME;
			this.rawDescription = DESCRIPTION;
		}
		initializeDescription();
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