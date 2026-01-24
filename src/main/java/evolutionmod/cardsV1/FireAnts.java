package evolutionmod.cardsV1;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.Drone;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.FireAntsPower;
import evolutionmod.orbsV1.*;

public class FireAnts
        extends BaseEvoCard {
    public static final String cardID = "FireAnts";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/FireAnts.png";
    private static final int COST = 1;
    private static final int EVOKE_AMT = 1;
    private static final int UPGRADE_EVOKE_AMT = 1;

    public FireAnts() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
//        this.magicNumber = this.baseMagicNumber = EVOKE_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        LavafolkGene orb = new LavafolkGene();
        addToBot(new ChannelAction(orb));
//        List<AbstractCard> drones = p.hand.group.stream()
//                .filter(card -> Drone.ID.equals(card.cardID))
//                .collect(Collectors.toList());
//        drones.forEach(card -> {
//            addToBot(new ExhaustSpecificCardAction(card, p.hand, true));
//            orb.onEvoke();
//        });
        if (!this.upgraded) {
//            formEffect(InsectGene.ID, () -> addToBot(new MakeTempCardInHandAction(new Drone())));
            formEffect(InsectGene.ID, () -> addToBot(new ApplyPowerAction(p, p, new FireAntsPower(p, EVOKE_AMT))));
        } else {
            addToBot(new ApplyPowerAction(p, p, new FireAntsPower(p, EVOKE_AMT)));
            formEffect(InsectGene.ID, () -> addToBot(new MakeTempCardInHandAction(new Drone())));
//            addToBot(new MakeTempCardInHandAction(new Drone()));
//            addToBot(new ChannelAction(new InsectGene()));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(UPGRADE_EVOKE_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(InsectGene.ID)) {
            this.glowColor = InsectGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}