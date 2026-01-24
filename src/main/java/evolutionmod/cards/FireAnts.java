package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene2;
import evolutionmod.orbs.LavafolkGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.FireAntsPower;

public class FireAnts
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:FireAnts";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/FireAnts.png";
    private static final int COST = 1;
    private static final int EVOKE_AMT = 1;

    public FireAnts() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
//        this.magicNumber = this.baseMagicNumber = EVOKE_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        LavafolkGene2 orb = new LavafolkGene2();
        addToBot(orb.getChannelAction());
//        List<AbstractCard> drones = p.hand.group.stream()
//                .filter(card -> Drone.ID.equals(card.cardID))
//                .collect(Collectors.toList());
//        drones.forEach(card -> {
//            addToBot(new ExhaustSpecificCardAction(card, p.hand, true));
//            orb.onEvoke();
//        });
        if (!this.upgraded) {
//            formEffect(InsectGene.ID, () -> addToBot(new MakeTempCardInHandAction(new Drone())));
            formEffect(InsectGene2.ID, () -> addToBot(new ApplyPowerAction(p, p, new FireAntsPower(p, EVOKE_AMT))));
        } else {
            addToBot(new ApplyPowerAction(p, p, new FireAntsPower(p, EVOKE_AMT)));
            formEffect(InsectGene2.ID, () -> addToBot(new MakeTempCardInHandAction(new Drone())));
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
        if (isPlayerInThisForm(InsectGene2.ID)) {
            this.glowColor = InsectGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}